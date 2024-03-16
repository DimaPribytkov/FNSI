package com.project.fnsi.dao;

import com.project.fnsi.entity.Passport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PassportRepository extends JpaRepository<Passport,Long> {
    @Query(value = "select p from Passport p where p.system =:system and p.version =:version")
    Optional<Passport> findOne(@Param("system") String system, @Param("version") String version);
}
