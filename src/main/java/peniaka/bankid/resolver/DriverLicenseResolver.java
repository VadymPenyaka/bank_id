package peniaka.bankid.resolver;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import peniaka.bankid.model.DriverLicenseDTO;
import peniaka.bankid.service.DriverLicenseService;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class DriverLicenseResolver {
    private final DriverLicenseService licenseService;
//TODO add exception
    @QueryMapping
    public DriverLicenseDTO driverLicense (@Argument UUID id) {
        System.out.println();
        return licenseService.getDriverLicenseById(id).orElseThrow();
    }
}
