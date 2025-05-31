package peniaka.bankid.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
    private final DriverLicenseService driverLicenseService;
    private final PersonDataGeneratorService personGenerationService;

    public Optional<PersonDTO> getPersonById (UUID id) {
        System.out.println("\n\n"+id.toString()+"\n\n");
        return Optional.ofNullable(personMapper.toDto(personRepository
                .findById(id).orElse(null)));
    }

    public void createPerson(PersonDTO person) {
        person.setDriverLicense(driverLicenseService.createDriverLicense(person.getDriverLicense()));
        personMapper.toDto(personRepository
                .save(personMapper.toEntity(person)));
    }

    public void createPersonFromRandomData (UUID personId) {
        PersonDTO personDTO = personGenerationService.generatePerson(personId);
        createPerson(personDTO);
    }
}
