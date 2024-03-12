package com.project.fnsi.service;

import com.project.fnsi.entity.Dictionary;

public interface DictionaryService {


    Dictionary getDictionary(String system, String version, String code);

    Dictionary addDictionary(Dictionary dictionary);

    Dictionary updateDictionary(Dictionary dictionary);

    void deleteDictionary(String system, String version, String code);
}
