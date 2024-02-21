package com.project.fnsi.dao;

import com.project.fnsi.entity.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {

    @Query(value = "select d from Dictionary d where d.system =:system and d.version =:version and d.code =:code")

    Dictionary findBySystemByVersionByCode(@Param("system") String system, @Param("version") String version, @Param("code") String code);

    @Query(value = "SELECT COUNT(d) FROM Dictionary d WHERE d.system = :system AND d.version = :version")
    Long countBySystemAndVersion(@Param("system") String system, @Param("version") String version);
}
