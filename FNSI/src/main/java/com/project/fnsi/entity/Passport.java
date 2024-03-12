package com.project.fnsi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "passports")

public class Passport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "\"system\"")
    private String system;
    private String version;
    private String data;
    public Passport(String system, String version) {
        this.system = system;
        this.version = version;
    }
}