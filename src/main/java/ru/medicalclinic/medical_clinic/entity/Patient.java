package ru.medicalclinic.medical_clinic.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id", nullable = false)
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "Name must contain only letters")
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @NotNull
    @Size(min = 1, max = 50)
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "Name must contain only letters")
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @NotNull
    @Past
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Pattern(regexp = "Male|Female", message = "Gender must be either Male or Female")
    @Column(name = "gender", length = 6)
    private String gender;

    @NotNull
    @Pattern(regexp = "^\\+7\\d{10}$", message = "Invalid phone number format")
    @Column(name = "phone_number", nullable = false,length = 12)
    private String phoneNumber;

    @NotNull
    @Pattern(regexp = "^\\d{16}$", message = "Insurance number must be exactly 16 digits")
    @Column(name = "insurance_number", nullable = false, unique = true,length = 16)
    private String insuranceNumber;

    @Pattern(regexp = "^(A|B|AB|O)[+-]$", message = "Invalid blood type")
    @Column(name = "blood_type", length = 3)
    private String bloodType;

    @Min(0)
    @Max(300)
    @Column(name = "height")
    private Float height;

    @Min(0)
    @Max(500)
    @Column(name = "weight")
    private Float weight;

    @NotNull
    @Column(name = "attached", nullable = false)
    private Boolean attached = false;

}