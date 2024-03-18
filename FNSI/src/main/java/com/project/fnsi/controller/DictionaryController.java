package com.project.fnsi.controller;

import com.project.fnsi.entity.Dictionary;
import com.project.fnsi.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/dictionaries")
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @Autowired
    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @GetMapping("/{system}/{version}/{code}")
    public Dictionary getDictionary(@PathVariable String system, @PathVariable String version, @PathVariable String code) {
        return dictionaryService.getDictionary(system, version, code);
    }

    @PutMapping("/")
    public Dictionary updateDictionary(@RequestBody Dictionary dictionary) {
        return dictionaryService.updateDictionary(dictionary);
    }


    @DeleteMapping("/{system}/{version}/{code}")
    public void deleteDictionary(@PathVariable String system, @PathVariable String version, @PathVariable String code) {
        dictionaryService.deleteDictionary(system, version, code);
    }
    @GetMapping("/update")
    public void upLoadDictionaries(){
        dictionaryService.updatePassportsAndLoadData();
    }
}
