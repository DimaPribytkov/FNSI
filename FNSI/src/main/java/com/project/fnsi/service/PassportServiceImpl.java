package com.project.fnsi.service;

import com.project.fnsi.dao.PassportRepository;
import com.project.fnsi.entity.Passport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PassportServiceImpl implements PassportService {

    private final PassportRepository passportRepository;
    @Autowired
    public PassportServiceImpl(PassportRepository passportRepository) {
        this.passportRepository = passportRepository;
    }

    @Override
    public Passport getPassportBySystemAndVersion(String system, String version) {
        return passportRepository.findOne(system, version).orElse(null);
    }

    @Override
    public void addPassport(Passport passport) {
        passportRepository.save(passport);
    }

    @Override
    public void updatePassport(Passport passport) {
        passportRepository.save(passport);
    }

    @Override
    public void removePassport(String system, String version) {
        Passport passport = passportRepository.findOne(system, version).orElse(null);
        if (passport==null){
            throw new RuntimeException("Невозможно удалить passport с системой " + system + " и версией " + version);
            //todo  заменить рантайм на кастомное исключение
        }
        passportRepository.delete(passport);
    }

    private void mappingRules(){
        //todo сделать позже.
    }
}
