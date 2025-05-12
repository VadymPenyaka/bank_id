package peniaka.bankid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import peniaka.bankid.entity.DriverLicense;

import java.util.UUID;

@Repository
public interface DriverLicenseRepository extends JpaRepository<DriverLicense, UUID> {

    boolean existsByDocumentNumber(String documentNumber);
}
