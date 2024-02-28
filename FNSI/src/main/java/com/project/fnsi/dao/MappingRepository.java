package com.project.fnsi.dao;

import com.project.fnsi.entity.Mapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MappingRepository extends JpaRepository<Mapping, Long> {

    @Query(value = "SELECT m FROM Mapping m WHERE " +
            "CASE " +
            "    WHEN (SELECT COUNT(*) FROM Mapping WHERE system = :system AND version = :version) > 0 " +
            "    THEN (m.system = :system AND m.version = :version) " +
            "    ELSE (m.system = :system AND m.version IS NULL) " +
            "END")
    Optional<Mapping> findAny(@Param("system") String system, @Param("version") String version);
}
