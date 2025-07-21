package br.com.portfolio.api.mapper;

import br.com.portfolio.api.dto.response.PersonResponse;
import br.com.portfolio.model.Person;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonMapper {
    public PersonResponse toPersonResponse(Person person) {
        if (person == null) {
            return null;
        }
        return PersonResponse.builder()
                .id(person.getId())
                .name(person.getName())
                .cpf(person.getCpf())
                .birthDate(person.getBirthDateFormatted()) // Assuming this method exists
                .role(person.getRole())
                .build();
    }

    public List<PersonResponse> toPersonResponseList(List<Person> people) {
        return people.stream()
                .map(this::toPersonResponse)
                .collect(Collectors.toList());
    }
}