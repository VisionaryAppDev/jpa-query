package com.example.jpaquery.controller;

import com.example.jpaquery.entity.Person;
import com.example.jpaquery.repository.PersonRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PersonController {

    private final EntityManager entityManager;
    private final PersonRepository personRepository;


    @GetMapping("/criteria-builder")
    public Object criteriaBuilder() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> criteriaQuery = builder.createQuery(Person.class);

        Root<Person> root = criteriaQuery.from(Person.class);
        Predicate ageGreaterThanOrEqualTo30 = builder.greaterThanOrEqualTo(root.get("age"), 30);
        Predicate idGreaterThanOrEqualTo25 = builder.greaterThanOrEqualTo(root.get("id"), 25);
        criteriaQuery.where(ageGreaterThanOrEqualTo30, idGreaterThanOrEqualTo25);

        TypedQuery<Person> query = entityManager.createQuery(criteriaQuery);
        List<Person> People = query.getResultList();
        return People;
    }


    /***
     * Check on Person's entity @NameQuery
     * @return
     */
    @GetMapping("/name-query")
    public Object NameQuery() {
        TypedQuery<Person> query = entityManager.createNamedQuery("WhatEver.findByAge", Person.class);
        query.setParameter("age", 30);
        List<Person> people = query.getResultList();
        return people;
    }


    @GetMapping("/pagination")
    public Object pagination(@PathVariable int age) {
        TypedQuery<Person> query = entityManager.createQuery("SELECT e FROM Person e WHERE e.age >= :age", Person.class);
        query.setParameter("age", age);
        query.setFirstResult(0); // set the first result to retrieve
        query.setMaxResults(10); // set the maximum number of results to retrieve
        List<Person> people = query.getResultList();
        return people;
    }


    @GetMapping("/projection")
    public Object projection() {
        TypedQuery<Object[]> query = entityManager.createQuery("SELECT e.id, e.firstName, e.lastName FROM Person e WHERE e.age >= :age", Object[].class);
        query.setParameter("age", 30);
        List<Object[]> results = query.getResultList();

        List<Person> summaries = new ArrayList<>();
        for (Object[] result : results) {
            Long id = (Long) result[0];
            String firstName = (String) result[1];
            String lastName = (String) result[2];

            Person person = new Person();
            person.setId(id);
            person.setFirstName(firstName);
            person.setLastName(lastName);
            summaries.add(person);
        }

        return summaries;
    }


    public static Specification<Person> isAdult() {
        return (root, query, cb) -> cb.greaterThan(root.get("age"), 18);
    }
    public static Specification<Person> hasNameStartingWith(String prefix) {
        return (root, query, cb) -> cb.like(root.get("firstName"), prefix + "%");
    }

    @GetMapping("specification")
    public Object specification() {
        return personRepository.findAll(isAdult().and(hasNameStartingWith("f")),
            Sort.by("firstName").ascending()
        );
    }
}
