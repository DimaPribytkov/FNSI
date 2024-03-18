package com.project.fnsi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.fnsi.dao.DictionaryRepository;
import com.project.fnsi.entity.Dictionary;
import com.project.fnsi.entity.Mapping;
import com.project.fnsi.entity.Passport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class DictionaryServiceImpl implements DictionaryService {

    private final DictionaryRepository dictionaryRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    @Value("${fnsi.key}")
    private String key;
    private final MappingService mappingService;
    private final PassportService passportService;
    @Value("${p.size}")
    private Integer pageSize;


    @Autowired
    public DictionaryServiceImpl(DictionaryRepository dictionaryRepository, RestTemplate restTemplate, ObjectMapper objectMapper, MappingService mappingService, PassportService passportService) {
        this.dictionaryRepository = dictionaryRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.mappingService = mappingService;
        this.passportService = passportService;
    }

    @Transactional
    @Override
    @Cacheable(value = "dictionariesCache", key = "#system + #version + #code", cacheManager = "cacheManager")
    public Dictionary getDictionary(String system, String version, String code) {
        Dictionary dictionary = dictionaryRepository.findOne(system, version, code).orElse(null);

        if (dictionary != null) {
            return dictionary;
        }
        Mapping mapping = mappingService.getMappingBySystemAndVersion(system, version);
        String data = restTemplate.getForObject("http://nsi.rosminzdrav.ru/port/rest/data?userKey=" + key + "&identifier=" + system + "&version=" + version + "&columns=" + mapping.getCode() + "," + mapping.getDisplay() + "&filters=" + mapping.getCode() + "|" + code, String.class);
        try {
            JsonNode jsonNode = objectMapper.readTree(data);
            if (!jsonNode.get("result").asText().equals("OK")) {
                throw new RuntimeException(jsonNode.get("resultText").asText());
            }
            String display = null;
            for (JsonNode node : jsonNode.withArray("list").get(0)) {
                if (node.get("column").asText().equals(mapping.getDisplay())) {
                    display = node.get("value").asText();
                    break;
                }
            }
            if (display == null) {
                throw new RuntimeException("Не удалось автоматически получить значение");//todo
            }
            dictionary = new Dictionary(null, system, version, code, display);
            return dictionaryRepository.save(dictionary);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    @Override
    public Dictionary addDictionary(Dictionary dictionary) {

        return dictionaryRepository.save(dictionary);
    }

    @Transactional
    @Override
    public Dictionary updateDictionary(Dictionary dictionary) {
        return dictionaryRepository.save(dictionary);
    }

    @Transactional
    @Override
    @CacheEvict(value = "dictionariesCache", key = "#system + #version + #code")
    public void deleteDictionary(String system, String version, String code) {
        Dictionary dictionary = dictionaryRepository.findOne(system, version, code)
                .orElseThrow(() ->
                        new EntityNotFoundException("Невозможно удалить Dictionary с системой " + system + ", версией "
                                + version + " и кодом " + code + ", так как Dictionary с этими параметрами отсутствует в базе данных"));

        dictionaryRepository.delete(dictionary);

    }
    @Scheduled(cron = "${task.expression.cron}")
    public void updatePassportsAndLoadData() {
        List<Passport> passports = passportService.getAllPassports();

        for (Passport passport : passports) {
            if (passport.getData() == null) {
                passport = passportService.getPassportFromUrl(passport.getSystem(), passport.getVersion());
            }
            Long rows;
            try {
                JsonNode jsonNode = objectMapper.readTree(passport.getData());
                Mapping mapping = mappingService.getMappingBySystemAndVersion(passport.getSystem(), passport.getVersion());
                rows = jsonNode.get("rowsCount").asLong();
                if (rows.equals(dictionaryRepository.rowsCount(passport.getSystem(), passport.getVersion()))) {
                    continue;
                }
                Long pageCount = rows / pageSize + 1;
                for (int i = 1; i <= pageCount; i++) {

                    String responseData = restTemplate.getForObject("http://nsi.rosminzdrav.ru/port/rest/data?userKey=" + key + "&identifier=" + passport.getSystem() + "&version=" + passport.getVersion() + "&page=" + i + "&size=" + pageSize + "&columns=" + mapping.getCode() + "," + mapping.getDisplay() + "&sorting=" + mapping.getDisplay(), String.class);
                    JsonNode responseNode = objectMapper.readTree(responseData);
                    for (JsonNode node : responseNode.withArray("list")) {
                        String code = null;
                        String display = null;
                        for (JsonNode dictNode : node) {
                            if (dictNode.get("column").asText().equals(mapping.getDisplay())) {
                                display = dictNode.get("value").asText();
                            }
                            if (dictNode.get("column").asText().equals(mapping.getCode())) {
                                code = dictNode.get("value").asText();
                            }
                        }
                        if(display != null && code != null){
                            Dictionary dictionary = dictionaryRepository.findOne(passport.getSystem(), passport.getVersion(), code).orElse(new Dictionary(null, passport.getSystem(), passport.getVersion(), code, display));
                            dictionaryRepository.save(dictionary);
                        }
                    }

                }


            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


