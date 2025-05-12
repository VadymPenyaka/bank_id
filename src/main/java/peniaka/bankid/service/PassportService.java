package peniaka.bankid.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import peniaka.bankid.mapper.PassportMapper;
import peniaka.bankid.model.PassportDTO;
import peniaka.bankid.repository.PassportRepository;

import java.util.Optional;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class PassportService {
    private final PassportRepository passportRepository;
    private final PassportMapper passportMapper;

    @Transactional
    public PassportDTO createPassport(PassportDTO passportDTO) {
        return passportMapper.toDto(passportRepository
                .save(passportMapper.toEntity(passportDTO)));
    }

    public Optional<PassportDTO> getPassportById(UUID id) {
        return Optional.ofNullable(passportMapper.toDto(passportRepository.findById(id).orElse(null)));
    }

    public boolean existsByDocumentNumber(String documentNumber) {
        return passportRepository.existsByDocumentNumber(documentNumber);
    }
}
