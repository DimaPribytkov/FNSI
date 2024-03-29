package com.project.fnsi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.fnsi.exeptions.CreatingMappingException;
import com.project.fnsi.dao.MappingRepository;
import com.project.fnsi.entity.Mapping;
import com.project.fnsi.entity.Passport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class MappingServiceImpl implements MappingService {
    private final MappingRepository mappingRepository;
    private final PassportService passportService;
    private final ObjectMapper objectMapper;

    @Autowired
    public MappingServiceImpl(MappingRepository mappingRepository, PassportService passportService, ObjectMapper objectMapper) {

        this.mappingRepository = mappingRepository;
        this.passportService = passportService;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public Mapping getMappingBySystemAndVersion(String system, String version) {
        Mapping mapping = mappingRepository.findAny(system, version).orElse(null);
        if (mapping != null) {
            return mapping;
        }
        Passport passport = passportService.getPassportBySystemAndVersion(system, version);
        if (passport.getData() == null) {
            passport = passportService.getPassportFromUrl(system, version);
        }
        try {
            JsonNode jsonNode = objectMapper.readTree(passport.getData());
            String code = null;
            String display = null;
            for (JsonNode key : jsonNode.withArray("keys")) {
                if (key.get("type").asText().equals("PRIMARY")) {
                    code = key.get("field").asText();
                }
                if (key.get("type").asText().equals("VALUE")) {
                    display = key.get("field").asText();
                }
            }
            if (code == null || display == null) {
                throw new CreatingMappingException("Для Passport с системой " + system + " и версией " + version + " невозможно " +
                        "автоматически создать маппинг полей, если одно или оба из них пустые");
            }
            mapping = new Mapping(null, system, version, code, display);
            return mappingRepository.save(mapping);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public Mapping addMapping(Mapping mapping) {
        return mappingRepository.save(mapping);
    }

    @Override
    @Transactional
    public Mapping updateMapping(Mapping mapping) {
        return mappingRepository.save(mapping);
    }

    @Override
    @Transactional
    public void removeMapping(String system, String version) {
        Mapping mapping = mappingRepository.findAny(system, version).orElseThrow(() -> new EntityNotFoundException("Невозможно " +
                "удалить Mapping с системой" + system + " и версией " + version + ", так как Mapping с этими параметрами отсутствует " +
                "в базе данных"));

        mappingRepository.delete(mapping);
    }

    @Override
    @Transactional
    public List<Mapping> getAllMapping() {
        return mappingRepository.findAll();
    }
}
