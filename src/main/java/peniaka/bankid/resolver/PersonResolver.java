package peniaka.bankid.resolver;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import peniaka.bankid.entity.Person;
import peniaka.bankid.exception.NorFoundException;
import peniaka.bankid.model.PersonDTO;
import peniaka.bankid.service.PersonService;

import java.util.UUID;
@Controller
@RequiredArgsConstructor
public class PersonResolver {
    private final PersonService personService;

    @QueryMapping
    public PersonDTO person (@Argument UUID id) {
        return personService.getPersonById(id).orElseThrow(()
                -> new NorFoundException("Personal info not found!"));
    }

    @MutationMapping
    public PersonDTO createPerson(PersonDTO person) {
        return personService.createPerson(person);
    }
}
