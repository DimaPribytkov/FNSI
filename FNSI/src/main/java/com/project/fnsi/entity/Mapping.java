package com.project.fnsi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Entity
@Table(name = "mappings")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Mapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "\"system\"")
    private String system;
    private double version;
    private String code;
    private String display;


}
