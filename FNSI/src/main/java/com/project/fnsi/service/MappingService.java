package com.project.fnsi.service;

import com.project.fnsi.entity.Mapping;

public interface MappingService {
    public Mapping getMappingBySystemAndVersion(String system, String version);

    public void addMapping(Mapping mapping);

    public void updateMapping(Mapping mapping);

    public void removeMapping(String system, String version);
}
