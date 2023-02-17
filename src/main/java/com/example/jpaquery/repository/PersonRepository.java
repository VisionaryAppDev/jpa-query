package com.example.jpaquery.repository;

import com.example.jpaquery.entity.Person;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findAll(Specification<Person> spec, Sort sort);

    @Query(nativeQuery = true, value = "SELECT * FROM customer WHERE age > :age AND last_name LIKE :namePrefix ORDER BY name ASC")
    List<Person> findCustomersByAgeAndNamePrefix(@Param("age") int age, @Param("namePrefix") String namePrefix);
}
