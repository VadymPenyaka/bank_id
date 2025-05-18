package peniaka.bankid.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import peniaka.bankid.entity.Person;
import peniaka.bankid.exception.NorFoundException;
import peniaka.bankid.mapper.PersonMapper;
import peniaka.bankid.model.PersonDTO;
import peniaka.bankid.repository.PersonRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public Optional<PersonDTO> getPersonById (UUID id) {
        return Optional.ofNullable(personMapper.toDto(personRepository
                .findById(id).orElse(null)));
    }

    public PersonDTO createPerson(PersonDTO person) {
        return personMapper.toDto(personRepository
                .save(personMapper.toEntity(person)));
    }
}
