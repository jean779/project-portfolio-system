package br.com.portfolio.service;

import br.com.portfolio.api.dto.request.CreateMemberRequest;
import br.com.portfolio.api.exception.ResourceNotFoundException;
import br.com.portfolio.model.Person;
import br.com.portfolio.model.enums.RoleType;
import br.com.portfolio.repository.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {

    private final PersonRepository personRepository;

    public Page<Person> findPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        log.info("Fetching paginated members - page: {}, size: {}", page, size);
        return personRepository.findAll(pageable);
    }

    @Transactional
    public Person createFromRequest(CreateMemberRequest request) {
        log.info("Creating new member: {}", request.getName());

        Person person = new Person();
        person.setName(request.getName());
        person.setCpf(request.getCpf());
        person.setBirthDate(LocalDate.parse(request.getBirthDate()));
        person.setEmployee(request.getRole() == RoleType.EMPLOYEE);
        person.setManager(request.getRole() == RoleType.MANAGER);

        Person saved = personRepository.save(person);
        log.info("Member created successfully with ID: {}", saved.getId());
        return saved;
    }

    @Transactional
    public Person update(Long id, CreateMemberRequest request) {
        log.info("Updating member with ID: {}", id);

        Person person = personRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Member not found for update - ID: {}", id);
                    return new ResourceNotFoundException("Member not found with ID: " + id);
                });

        person.setName(request.getName());
        person.setCpf(request.getCpf());
        person.setBirthDate(LocalDate.parse(request.getBirthDate()));
        person.setEmployee(request.getRole() == RoleType.EMPLOYEE);
        person.setManager(request.getRole() == RoleType.MANAGER);

        Person updated = personRepository.save(person);
        log.info("Member updated successfully - ID: {}", updated.getId());
        return updated;
    }

    @Transactional
    public void delete(Long id) {
        log.info("Attempting to delete member with ID: {}", id);

        Person person = personRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Member not found for deletion - ID: {}", id);
                    return new ResourceNotFoundException("Member not found with ID: " + id);
                });

        personRepository.delete(person);
        log.info("Member deleted successfully - ID: {}", id);
    }

    public List<Person> findAllManagers() {
        log.info("Fetching all members with MANAGER role");
        return personRepository.findByManagerTrue();
    }

    public List<Person> findAllEmployees() {
        log.info("Fetching all members with EMPLOYEE role");
        return personRepository.findByEmployeeTrue();
    }

    public Person findById(Long id) {
        log.info("Fetching member by ID: {}", id);
        return personRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Member not found with ID: {}", id);
                    return new ResourceNotFoundException("Member not found with ID: " + id);
                });
    }
}
