package br.com.portfolio.repository;

import br.com.portfolio.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findByManagerTrue();

    List<Person> findByEmployeeTrue();
}

