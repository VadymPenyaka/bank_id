package peniaka.bankid.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(updatable = false, nullable = false, unique = true)
    private UUID id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private DriverLicense driverLicense;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Passport passport;
}
