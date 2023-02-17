package com.example.jpaquery.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@NamedQuery(name = "WhatEver.findByAge", query = "SELECT e FROM Person e WHERE e.age >= :age")
@Entity
public class Person {

    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private int age;
    private String gender;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
