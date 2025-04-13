package peniaka.bankid.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import peniaka.bankid.generator.DriverLicenseGenerator;
import peniaka.bankid.generator.PassportGenerator;
import peniaka.bankid.model.PersonalDataDto;
import peniaka.bankid.model.DriverLicenseDTO;
import peniaka.bankid.model.PassportDTO;

@RestController
@RequestMapping(PersonalInfoController.BASE_PATH)
@RequiredArgsConstructor
public class PersonalInfoController {
    public final static String BASE_PATH = "/api/v1/info";
    private final DriverLicenseGenerator driverLicenseGenerator;
    private final PassportGenerator passportGenerator;

    @GetMapping("/get")
    public PersonalDataDto getPersonalInfo () {
        PassportDTO passport = passportGenerator.generateRandomPassportDTO();
        DriverLicenseDTO license = driverLicenseGenerator.generateRandomDriverLicenseDTO();

        return PersonalDataDto.builder()
                .driverLicense(license)
                .passport(passport)
                .build();
    }
}
