package peniaka.bankid.resolver;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import peniaka.bankid.exception.NorFoundException;
import peniaka.bankid.model.DriverLicenseDTO;
import peniaka.bankid.service.DriverLicenseService;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class DriverLicenseResolver {
    private final DriverLicenseService licenseService;

    @QueryMapping
    public DriverLicenseDTO driverLicense (@Argument UUID id) {
        System.out.println();
        return licenseService.getDriverLicenseById(id).orElseThrow(()
                -> new NorFoundException("Driver license not found!"));
    }
}
