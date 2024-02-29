package com.project.fnsi.dao;

import com.project.fnsi.entity.Mapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MappingRepository extends JpaRepository<Mapping, Long> {

    @Query(value =
            "SELECT m FROM Mapping m " +
            "WHERE m.system = :system " +
                    "AND (m.version = :version " +
                    "OR (m.version IS NULL AND NOT EXISTS (" +
                    "SELECT m2 FROM Mapping m2 " +
                    "WHERE m2.system = :system " +
                    "AND m2.version = :version)))")
    Optional<Mapping> findAny(@Param("system") String system, @Param("version") String version);
}
