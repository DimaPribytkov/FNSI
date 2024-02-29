package com.project.fnsi.service;

import com.project.fnsi.dao.DictionaryRepository;
import com.project.fnsi.entity.Dictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DictionaryServiceImpl implements DictionaryService {

    private final DictionaryRepository dictionaryRepository;

    @Autowired
    public DictionaryServiceImpl(DictionaryRepository dictionaryRepository) {
        this.dictionaryRepository = dictionaryRepository;
    }

    @Transactional
    @Override
    public Dictionary getDirectory(String system, String version, String code) {
        return dictionaryRepository.findOne(system, version, code).orElse(null);
    }

    @Transactional
    @Override
    public Dictionary addDirectory(Dictionary dictionary) {
        return dictionaryRepository.save(dictionary);
    }

    @Transactional
    @Override
    public Dictionary updateDirectory(Dictionary dictionary) {
        return dictionaryRepository.save(dictionary);
    }

    @Transactional
    @Override
    public void deleteDictionary(String system, String version, String code) {
        Dictionary dictionary = dictionaryRepository.findOne(system, version, code)
                .orElseThrow(() ->
                        new RuntimeException("Справочника с такими системой, " + system + " версией " + version + " и кодом " + code + "нету."));
        dictionaryRepository.delete(dictionary);

    }
}
