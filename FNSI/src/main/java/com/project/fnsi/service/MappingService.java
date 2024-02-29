package com.project.fnsi.service;

import com.project.fnsi.entity.Mapping;

   public interface MappingService {
    Mapping getMappingBySystemAndVersion(String system, String version);

    Mapping addMapping(Mapping mapping);

    Mapping updateMapping(Mapping mapping);

    void removeMapping(String system, String version);
}
