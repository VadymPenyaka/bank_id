package peniaka.bankid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import peniaka.bankid.entity.Passport;

import java.util.UUID;

@Repository
public interface PassportRepository extends JpaRepository<Passport, UUID> {
    boolean existsByDocumentNumber(String documentNumber);
}
