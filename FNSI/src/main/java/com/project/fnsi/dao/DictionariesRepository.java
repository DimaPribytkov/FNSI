package com.project.fnsi.dao;

import com.project.fnsi.entity.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DictionariesRepository extends JpaRepository<Dictionary,Long> {
}
