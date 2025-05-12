package peniaka.bankid.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import peniaka.bankid.model.LicenseCategory;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DriverLicenseCategory {
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(updatable = false, nullable = false, unique = true, columnDefinition = "VARCHAR(36)")
    private UUID id;

    @ManyToOne
    @JoinColumn(nullable = false, columnDefinition = "varchar(36)")
    private DriverLicense license;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private LicenseCategory category;

    @Column(nullable = false)
    private LocalDate issueDate;
}

