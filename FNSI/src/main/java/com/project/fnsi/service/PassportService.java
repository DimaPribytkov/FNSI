package com.project.fnsi.service;

import com.project.fnsi.entity.Mapping;
import com.project.fnsi.entity.Passport;

public interface PassportService {
    public Passport getPassportBySystemAndVersion(String system, String version);

    public void addPassport(Passport passport);

    public void updatePassport(Passport passport);

    public void removePassport(String system, String version);
}
