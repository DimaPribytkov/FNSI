package com.project.fnsi.dao;

import com.project.fnsi.entity.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {

    @Query(value = "select d from Dictionary d where d.system =:system and d.version =:version and d.code =:code")
    Optional<Dictionary> findOne(@Param("system") String system, @Param("version") String version, @Param("code") String code);

    @Query(value = "select count(d) from Dictionary d where d.system = :system and d.version = :version")
    Long rowsCount(@Param("system") String system, @Param("version") String version);
}
