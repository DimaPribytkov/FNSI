package com.project.fnsi.service;

import com.project.fnsi.dao.PassportRepository;
import com.project.fnsi.entity.Passport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PassportServiceImpl implements PassportService {

    private final PassportRepository passportRepository;
    @Autowired
    public PassportServiceImpl(PassportRepository passportRepository) {
        this.passportRepository = passportRepository;
    }

    @Override
    @Transactional
    public Passport getPassportBySystemAndVersion(String system, String version) {
        return passportRepository.findOne(system, version).orElse(null);
    }

    @Override
    @Transactional
    public Passport addPassport(Passport passport) {
        return passportRepository.save(passport);
    }

    @Override
    @Transactional
    public Passport updatePassport(Passport passport) {
        return passportRepository.save(passport);
    }

    @Override
    @Transactional
    public void removePassport(String system, String version) {
        Passport passport = passportRepository.findOne(system, version).orElseThrow(()->new RuntimeException("Невозможно " +
                "удалить passport с системой" + system + " и версией "  + version));

        passportRepository.delete(passport);
    }

    private void mappingRules(){
        //todo сделать позже.
    }
}
