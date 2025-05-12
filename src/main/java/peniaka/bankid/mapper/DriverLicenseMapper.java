package peniaka.bankid.mapper;

import org.mapstruct.Mapper;
import peniaka.bankid.entity.DriverLicense;
import peniaka.bankid.model.DriverLicenseDTO;

@Mapper
public interface DriverLicenseMapper {
    DriverLicense toDto(DriverLicenseDTO driverLicensesDTO);

    DriverLicenseDTO toEntity(DriverLicense entity);
}
