package peniaka.bankid.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import peniaka.bankid.entity.DriverLicense;
import peniaka.bankid.entity.DriverLicenseCategory;
import peniaka.bankid.model.DriverLicenseDTO;
import peniaka.bankid.repository.DriverLicenseCategoryRepository;
import peniaka.bankid.repository.DriverLicenseRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class DriverLicenseService {
    private final peniaka.bankid.mapper.DriverLicenseMapper driverLicenseMapper;
    private final DriverLicenseRepository driverLicensesRepository;
    private final DriverLicenseCategoryRepository categoryRepository;

    public @Valid @NotNull(message = "Driver license credentials is required!") DriverLicenseDTO createDriverLicense(DriverLicenseDTO driverLicensesDTO) {
        DriverLicense driverLicense = driverLicenseMapper.toEntity(driverLicensesDTO);
        DriverLicense saved = driverLicensesRepository.save(driverLicense);

        Set<DriverLicenseCategory> categories = driverLicense.getCategories();
        for (DriverLicenseCategory category : categories) {
            category.setLicense(saved);
        }

        categoryRepository.saveAll(categories);

        return driverLicenseMapper.toDto(driverLicensesRepository.save(saved));
    }

    public boolean existsByDocumentNumber(String documentNumber) {
        return driverLicensesRepository.existsByDocumentNumber(documentNumber);
    }
}
