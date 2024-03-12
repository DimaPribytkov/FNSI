package com.project.fnsi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.fnsi.dao.PassportRepository;
import com.project.fnsi.entity.Passport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class PassportServiceImpl implements PassportService {
    private final ObjectMapper objectMapper;
    private final PassportRepository passportRepository;
    private final RestTemplate restTemplate;
    @Value("${fnsi.key}")
    private String key;

    @Autowired
    public PassportServiceImpl(ObjectMapper objectMapper, PassportRepository passportRepository, RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.passportRepository = passportRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    @Transactional
    public Passport getPassportBySystemAndVersion(String system, String version) {
        Passport passport = passportRepository.findOne(system, version).orElse(null);
        if (passport != null && passport.getData() != null) {
            return passport;
        }
        return getPassportFromUrl(system, version);
    }

    @Override
    @Transactional
    public Passport getPassportFromUrl(String system, String version) {
        String data = restTemplate.getForObject("http://nsi.rosminzdrav.ru/port/rest/passport?userKey=" + key + "&identifier=" + system + "&version=" + version, String.class);
        try {
            JsonNode jsonNode = objectMapper.readTree(data);
            if(!jsonNode.get("result").asText().equals("OK")) {
                throw new RuntimeException(jsonNode.get("resultText").asText());
            }
            Passport passport = passportRepository.findOne(system, version).orElse(new Passport(system, version));
            passport.setData(data);
            return passportRepository.save(passport);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public Passport addPassport(Passport passport) {
        return passportRepository.save(passport);
    }

    @Override
    @Transactional
    public Passport updatePassport(Passport passport) {
        return passportRepository.save(passport);
    }

    @Override
    @Transactional
    public void removePassport(String system, String version) {
        Passport passport = passportRepository.findOne(system, version).orElseThrow(() -> new RuntimeException("Невозможно " +
                "удалить passport с системой" + system + " и версией " + version));

        passportRepository.delete(passport);
    }

    private void mappingRules() {
        //todo сделать позже.
    }

}
