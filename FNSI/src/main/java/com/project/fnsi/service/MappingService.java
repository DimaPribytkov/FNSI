package com.project.fnsi.service;

import com.project.fnsi.entity.Mapping;

import java.util.List;

public interface MappingService {
    Mapping getMappingBySystemAndVersion(String system, String version);


    Mapping addMapping(Mapping mapping);

    Mapping updateMapping(Mapping mapping);

    void removeMapping(String system, String version);

    List<Mapping> getAllMapping();
}
