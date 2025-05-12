package peniaka.bankid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import peniaka.bankid.entity.DriverLicenseCategory;

import java.util.UUID;

public interface DriverLicenseCategoryRepository extends JpaRepository<DriverLicenseCategory, UUID> {
}
