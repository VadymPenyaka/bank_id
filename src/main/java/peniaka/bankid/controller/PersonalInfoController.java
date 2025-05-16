package peniaka.bankid.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import peniaka.bankid.model.CustomerFullInfoRequest;
import peniaka.bankid.model.DriverLicenseDTO;
import peniaka.bankid.model.PassportDTO;
import peniaka.bankid.service.DriverLicenseService;
import peniaka.bankid.service.PassportService;
import peniaka.bankid.util.DriverLicenseGenerator;
import peniaka.bankid.util.PassportGenerator;

@RestController
@RequestMapping(PersonalInfoController.BASE_PATH)
@RequiredArgsConstructor
public class PersonalInfoController {
    public final static String BASE_PATH = "/api/v1/info";
    private final DriverLicenseGenerator driverLicenseGenerator;
    private final PassportGenerator passportGenerator;
    private final PassportService passportService;
    private final DriverLicenseService driverLicenseService;

    @PostMapping("/createRandomPerson")
    public ResponseEntity<?> createPersonalRandomInfo() {
        PassportDTO passport = passportGenerator.generateRandomPassportDTO();
        DriverLicenseDTO license = driverLicenseGenerator.generateRandomDriverLicenseDTO();
        System.out.println(license);
        passportService.createPassport(passport);
        driverLicenseService.createDriverLicense(license);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PostMapping("/verifyPersonalData")
    public ResponseEntity<?> addCustomerPersonalInfo(@Valid @RequestBody CustomerFullInfoRequest customerRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        passportService.createPassport(customerRequest.getPassport());
        driverLicenseService.createDriverLicense(customerRequest.getDriverLicense());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
