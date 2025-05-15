package peniaka.bankid.mapper;

import org.mapstruct.Mapper;
import peniaka.bankid.entity.DriverLicense;
import peniaka.bankid.model.DriverLicenseDTO;

@Mapper
public interface DriverLicenseMapper {
    DriverLicenseDTO toDto(DriverLicense entity);

    DriverLicense toEntity(DriverLicenseDTO driverLicensesDTO);
}
