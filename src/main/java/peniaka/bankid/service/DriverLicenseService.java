package peniaka.bankid.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import peniaka.bankid.entity.DriverLicense;
import peniaka.bankid.entity.DriverLicenseCategory;
import peniaka.bankid.model.DriverLicenseDTO;
import peniaka.bankid.repository.DriverLicenseCategoryRepository;
import peniaka.bankid.repository.DriverLicenseRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class DriverLicenseService {
    private final peniaka.bankid.mapper.DriverLicenseMapper driverLicenseMapper;
    private final DriverLicenseRepository driverLicensesRepository;
    private final DriverLicenseCategoryRepository licenseCategoryRepository;

    public DriverLicenseDTO createDriverLicense(DriverLicenseDTO driverLicensesDTO) {
        DriverLicense license = driverLicenseMapper.toDto(driverLicensesDTO);
        DriverLicense saved = driverLicensesRepository
                .save(license);
        Set<DriverLicenseCategory> categories = license.getCategories();
        for (DriverLicenseCategory category : categories) {
            category.setLicense(saved);
        }

        licenseCategoryRepository.saveAll(categories);

        return driverLicenseMapper
                .toEntity(driverLicensesRepository.save(saved));
    }

    public Optional<DriverLicenseDTO> getDriverLicenseById(UUID id) {
        return Optional.ofNullable(driverLicenseMapper
                .toEntity(driverLicensesRepository
                        .findById(id).orElse(null)));
    }

    public Optional<DriverLicenseDTO> getDtoFromImage(String url) {
        return Optional.empty();
    }

    public boolean existsByDocumentNumber(String documentNumber) {
        return driverLicensesRepository.existsByDocumentNumber(documentNumber);
    }
}
