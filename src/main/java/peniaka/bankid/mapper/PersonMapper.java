package peniaka.bankid.mapper;

import peniaka.bankid.entity.Person;
import peniaka.bankid.model.PersonDTO;

public interface PersonMapper {
    PersonDTO toDto (Person person);

    Person toEntity (PersonDTO personDTO);
}
