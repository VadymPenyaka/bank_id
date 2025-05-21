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
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverLicense {
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(updatable = false, nullable = false, unique = true, columnDefinition = "VARCHAR(36)")
    private UUID id;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @Column(nullable = false)
    @Convert(converter = SensitiveDataConverter.class)
    private String issuedBy;

    @Column(nullable = false, unique = true)
    @Convert(converter = SensitiveDataConverter.class)
    private String documentNumber;

    @OneToMany(mappedBy = "license", fetch = FetchType.EAGER)
    private Set<DriverLicenseCategory> categories = new HashSet<>();

    @OneToOne(mappedBy = "driverLicense")
    private Person person;
}
