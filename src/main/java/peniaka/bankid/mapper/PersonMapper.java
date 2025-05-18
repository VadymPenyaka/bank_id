package peniaka.bankid.mapper;

import org.mapstruct.Mapper;
import peniaka.bankid.entity.Person;
import peniaka.bankid.model.PersonDTO;

@Mapper
public interface PersonMapper {
    PersonDTO toDto (Person person);

    Person toEntity (PersonDTO personDTO);
}
