package com.project.fnsi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.fnsi.dao.DictionaryRepository;
import com.project.fnsi.entity.Dictionary;
import com.project.fnsi.entity.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;

@Service
public class DictionaryServiceImpl implements DictionaryService {

    private final DictionaryRepository dictionaryRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    @Value("${fnsi.key}")
    private String key;
    private final MappingService mappingService;

    @Autowired
    public DictionaryServiceImpl(DictionaryRepository dictionaryRepository, RestTemplate restTemplate, ObjectMapper objectMapper, MappingService mappingService) {
        this.dictionaryRepository = dictionaryRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.mappingService = mappingService;
    }

    @Transactional
    @Override
    @Cacheable(value = "dictionariesCach", key = "#system + #version + #code", cacheManager = "cacheManager")
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
    @CacheEvict(value = "dictionariesCach", key = "#system + #version + #code")
    public void deleteDictionary(String system, String version, String code) {
        Dictionary dictionary = dictionaryRepository.findOne(system, version, code)
                .orElseThrow(() ->
                        new EntityNotFoundException("Невозможно удалить Dictionary с системой " + system + ", версией "
                                + version + " и кодом " + code + ", так как Dictionary с этими параметрами отсутствует в базе данных"));

        dictionaryRepository.delete(dictionary);

    }
}
