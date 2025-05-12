package peniaka.bankid.mapper;

import org.mapstruct.Mapper;
import peniaka.bankid.entity.Passport;
import peniaka.bankid.model.PassportDTO;

@Mapper
public interface PassportMapper {
    PassportDTO toDto(Passport passport);

    Passport toEntity(PassportDTO passportDTO);
}
