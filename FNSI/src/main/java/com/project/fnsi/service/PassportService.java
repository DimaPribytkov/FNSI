package com.project.fnsi.service;

import com.project.fnsi.entity.Mapping;
import com.project.fnsi.entity.Passport;

    public interface PassportService {
    Passport getPassportBySystemAndVersion(String system, String version);

    Passport addPassport(Passport passport);

    Passport updatePassport(Passport passport);

    void removePassport(String system, String version);
}