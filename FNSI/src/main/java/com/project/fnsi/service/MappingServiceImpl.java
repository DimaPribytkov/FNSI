package com.project.fnsi.service;

import com.project.fnsi.dao.MappingRepository;
import com.project.fnsi.entity.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.yaml.snakeyaml.nodes.NodeId.mapping;

@Service
public class MappingServiceImpl implements MappingService {
    private final MappingRepository mappingRepository;
    @Autowired
    public MappingServiceImpl(MappingRepository mappingRepository) {
        this.mappingRepository = mappingRepository;
    }

    @Override
    @Transactional
    public Mapping getMappingBySystemAndVersion(String system, String version) {
        return mappingRepository.findAny(system, version).orElse(null);
    }

    @Override
    @Transactional
    public void addMapping(Mapping mapping) {
        mappingRepository.save(mapping);
    }

    @Override
    @Transactional
    public void updateMapping(Mapping mapping) {
        mappingRepository.save(mapping);
    }

    @Override
    @Transactional
    public void removeMapping(String system, String version) {
        Mapping mapping = mappingRepository.findAny(system, version).orElse(null);
        if (mapping==null){
            throw new RuntimeException("Невозможно удалить mapping с системой " + system + " и версией " + version);
        //todo  заменить рантайм на кастомное исключение
        }
        mappingRepository.delete(mapping);
    }
}
