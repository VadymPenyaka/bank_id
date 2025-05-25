package peniaka.bankid.resolver;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import peniaka.bankid.exception.NotFoundException;
import peniaka.bankid.model.PersonDTO;
import peniaka.bankid.service.PersonService;

import java.util.UUID;
@Controller
@RequestMapping(PersonResolver.BASE_PATH)
@RequiredArgsConstructor
public class PersonResolver {
    private final PersonService personService;
    public static final String BASE_PATH = "/api/v1/person";

    @QueryMapping
    public PersonDTO person (@Argument UUID id) {
        return personService.getPersonById(id).orElseThrow(()
                -> new NotFoundException("Personal info not found!"));
    }

    @MutationMapping
    public ResponseEntity<?> createPerson(@Argument("person") PersonDTO person) {
        personService.createPerson(person);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
