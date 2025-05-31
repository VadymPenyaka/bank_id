package peniaka.bankid.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import peniaka.bankid.service.PersonService;

import java.util.UUID;

@RestController
@RequestMapping(PersonController.BASE_PATH)
@RequiredArgsConstructor
public class PersonController {
    public final static String BASE_PATH = "/api/v1/person";
    private final PersonService personService;

    @PostMapping("/{id}")
    public ResponseEntity<?> createPersonWithRandomData (@PathVariable UUID id) {
        personService.createPersonFromRandomData(id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
