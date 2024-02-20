package com.project.fnsi.dao;

import com.project.fnsi.entity.Passport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassportRepository extends JpaRepository<Passport,Long> {
}
