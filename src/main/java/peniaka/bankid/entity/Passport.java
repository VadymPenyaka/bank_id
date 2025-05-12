package peniaka.bankid.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import peniaka.bankid.util.SensitiveDataConverter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Passport {
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(updatable = false, nullable = false, unique = true)
    private UUID id;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false, unique = true)
    @Convert(converter = SensitiveDataConverter.class)
    private String documentNumber;

    @Column(nullable = false)
    @Convert(converter = SensitiveDataConverter.class)
    private String issuedBy;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @Column
    @Convert(converter = SensitiveDataConverter.class)
    private String taxIdentificationNumber;

    @OneToOne(mappedBy = "passport")
    private Person person;
}
