package com.project.fnsi.dao;

import com.project.fnsi.entity.Mapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MappingRepository extends JpaRepository<Mapping,Long> {

    @Query(value = "select m from Mapping m where m.system =:system and (m.version =:version OR m.version IS NULL )")
    Optional<Mapping> findAny(@Param("system") String system, @Param("version") String version);

}
