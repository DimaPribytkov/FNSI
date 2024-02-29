package com.project.fnsi.service;

import com.project.fnsi.entity.Dictionary;

public interface DictionaryService {


    Dictionary getDirectory(String system, String version, String code);

    Dictionary addDirectory(Dictionary dictionary);

    Dictionary updateDirectory(Dictionary dictionary);

    void deleteDictionary(String system, String version, String code);
}
