package com.project.fnsi.controllers;

import com.project.fnsi.dao.MappingRepository;
import com.project.fnsi.entity.Mapping;
import com.project.fnsi.service.MappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mappings")
public class MappingController {
    private final MappingService mappingService;
    @Autowired
    public MappingController(MappingService mappingService) {
        this.mappingService = mappingService;

    }
    @GetMapping("/")
    public List<Mapping> getAllMapping() {
        return mappingService.getAllMapping();
    }
    @GetMapping("/{system}/{version}")
    public Mapping getMapping(@PathVariable String system, @PathVariable String version) {
        return mappingService.getMappingBySystemAndVersion(system, version);
    }

    @PostMapping("/")
    public Mapping addMapping(@RequestBody Mapping mapping) {
        return mappingService.addMapping(mapping);
    }
    @PutMapping("/")
    public Mapping updateMapping(@RequestBody Mapping mapping) {
        return mappingService.updateMapping(mapping);
    }
    @DeleteMapping("/{system}/{version}")
    public void deleteMapping(@PathVariable String system, @PathVariable String version) {
        mappingService.removeMapping(system, version);
    }


}
