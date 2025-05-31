package peniaka.bankid.service;

import org.springframework.stereotype.Service;
import peniaka.bankid.model.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@Service
public class PersonDataGeneratorService {

    private static final Random random = new Random();

    private static final String[] FIRST_NAMES = {
            "Alexander", "Sergey", "Andrew", "Vladimir", "Vasily", "Nicholas", "Ivan", "Peter",
            "Elena", "Natalia", "Irina", "Svetlana", "Maria", "Tatiana", "Ludmila", "Anna",
            "Dmitry", "Yury", "Victor", "Bogdan", "Maxim", "Roman", "Yaroslav", "Denis",
            "Michael", "Daniel", "James", "John", "Robert", "William", "David", "Richard",
            "Sarah", "Jessica", "Jennifer", "Lisa", "Michelle", "Kimberly", "Susan", "Karen"
    };

    private static final String[] LAST_NAMES = {
            "Kovalenko", "Shevchenko", "Boyko", "Tkachenko", "Kravchenko", "Melnik", "Polishchuk",
            "Lysenko", "Ivanenko", "Petrenko", "Sidorenko", "Moroz", "Pavlenko", "Savchenko",
            "Rudenko", "Marchenko", "Lytvynenko", "Gritsenko", "Kuzmenko", "Onishchenko",
            "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez",
            "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor"
    };

    private static final String[] MIDDLE_NAMES = {
            "Alexandrovich", "Sergeevich", "Andreevich", "Vladimirovich", "Vasilievich",
            "Nikolaevich", "Ivanovich", "Petrovich", "Dmitrievich", "Yurievich",
            "Alexandrovna", "Sergeevna", "Andreevna", "Vladimirovna", "Vasilievna",
            "Nikolaevna", "Ivanovna", "Petrovna", "Dmitrievna", "Yurievna",
            "James", "Robert", "John", "Michael", "William", "David", "Richard", "Charles",
            "Marie", "Rose", "Grace", "Hope", "Faith", "Joy", "Anne", "Lynn"
    };

    private static final String[] PASSPORT_ISSUED_BY = {
            "1234", "2345", "3456", "4567", "5678", "6789", "7890", "8901", "9012", "0123",
            "1111", "2222", "3333", "4444", "5555", "6666", "7777", "8888", "9999", "0000"
    };

    private static final String[] LICENSE_ISSUED_BY = {
            "1001", "1002", "1203", "1064", "1065", "3201", "2202", "2013", "2004", "2015",
            "3191", "3992", "3333", "3044", "3115", "4231", "4052", "4803", "4004", "4095"
    };

    public PersonDTO generatePerson(UUID personId) {
        String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
        String middleName = MIDDLE_NAMES[random.nextInt(MIDDLE_NAMES.length)];

        String fullName = firstName + " " + middleName + " " + lastName;
        LocalDate dateOfBirth = generateDateOfBirth();

        return PersonDTO.builder()
                .id(personId)
                .passport(generatePassport(fullName, dateOfBirth))
                .driverLicense(generateDriverLicense(dateOfBirth))
                .build();
    }

    private PassportDTO generatePassport(String fullName, LocalDate dateOfBirth) {
        LocalDate issueDate = generatePassportIssueDate(dateOfBirth);

        return PassportDTO.builder()
                .fullName(fullName)
                .dateOfBirth(dateOfBirth)
                .documentNumber(generatePassportNumber())
                .issuedBy(PASSPORT_ISSUED_BY[random.nextInt(PASSPORT_ISSUED_BY.length)])
                .expirationDate(issueDate.plusYears(10)) // Passport valid for 10 years
                .taxIdentificationNumber(generateTaxNumber())
                .build();
    }

    private DriverLicenseDTO generateDriverLicense(LocalDate dateOfBirth) {
        LocalDate issueDate = generateLicenseIssueDate(dateOfBirth);

        return DriverLicenseDTO.builder()
                .issueDate(issueDate)
                .expirationDate(issueDate.plusYears(30)) // License valid for 30 years
                .issuedBy(LICENSE_ISSUED_BY[random.nextInt(LICENSE_ISSUED_BY.length)])
                .documentNumber(generateLicenseNumber())
                .categories(generateLicenseCategories(issueDate))
                .build();
    }

    private Set<DriverLicenseCategoryDTO> generateLicenseCategories(LocalDate licenseIssueDate) {
        Set<DriverLicenseCategoryDTO> categories = new HashSet<>();

        categories.add(DriverLicenseCategoryDTO.builder()
                .category(LicenseCategory.B)
                .issueDate(licenseIssueDate)
                .build());

        LicenseCategory[] allCategories = LicenseCategory.values();
        int numberOfCategories = random.nextInt(3) + 1;

        for (int i = 0; i < numberOfCategories && categories.size() < 4; i++) {
            LicenseCategory category = allCategories[random.nextInt(allCategories.length)];
            if (categories.stream().noneMatch(c -> c.getCategory() == category)) {
                LocalDate categoryIssueDate = licenseIssueDate.plusDays(random.nextInt(365 * 5)); // Up to 5 years later
                categories.add(DriverLicenseCategoryDTO.builder()
                        .category(category)
                        .issueDate(categoryIssueDate)
                        .build());
            }
        }

        return categories;
    }

    private LocalDate generateDateOfBirth() {
        LocalDate now = LocalDate.now();
        int age = 18 + random.nextInt(42);
        return now.minusYears(age).minusDays(random.nextInt(365));
    }

    private LocalDate generatePassportIssueDate(LocalDate dateOfBirth) {
        LocalDate earliestIssue = dateOfBirth.plusYears(16);
        LocalDate now = LocalDate.now();
        long daysBetween = earliestIssue.toEpochDay() - now.toEpochDay();

        if (daysBetween >= 0) {
            return now.minusDays(random.nextInt(365 * 5)); // Last 5 years
        } else {
            long daysRange = now.toEpochDay() - earliestIssue.toEpochDay();
            return earliestIssue.plusDays(random.nextLong() % daysRange);
        }
    }

    private LocalDate generateLicenseIssueDate(LocalDate dateOfBirth) {
        LocalDate earliestIssue = dateOfBirth.plusYears(18);
        LocalDate now = LocalDate.now();
        long daysBetween = earliestIssue.toEpochDay() - now.toEpochDay();

        if (daysBetween >= 0) {
            return now.minusDays(random.nextInt(365 * 5)); // Last 5 years
        } else {
            long daysRange = now.toEpochDay() - earliestIssue.toEpochDay();
            return earliestIssue.plusDays(random.nextLong() % daysRange);
        }
    }

    private String generatePassportNumber() {
        int number = 100000000 + random.nextInt(900000000);
        return String.valueOf(number);
    }

    private String generateLicenseNumber() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sb.append((char) ('A' + random.nextInt(26)));
        }
        sb.append(String.format("%06d", random.nextInt(1000000)));
        return sb.toString();
    }

    private String generateTaxNumber() {
        long taxNumber = 1000000000L + (long) (random.nextDouble() * 9000000000L);
        return String.valueOf(taxNumber);
    }

}
