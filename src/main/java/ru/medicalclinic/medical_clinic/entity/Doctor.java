package ru.medicalclinic.medical_clinic.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id", nullable = false)
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
    @Pattern(regexp = "^[a-zA-Zа-яА-Я\\s]+$", message = "Specialty must contain only letters and spaces")
    @Column(name = "specialty", nullable = false, length = Integer.MAX_VALUE)
    private String specialty;

    @NotNull
    @Pattern(regexp = "^\\+7\\d{10}$", message = "Invalid phone number format")
    @Column(name = "phone_number", nullable = false, length = 12)
    private String phoneNumber;

    @NotNull
    @Column(name = "work_start_time", nullable = false)
    private LocalTime workStartTime;

    @NotNull
    @Column(name = "work_end_time", nullable = false)
    private LocalTime workEndTime;

    @Min(0)
    @Max(100)
    @Column(name = "experience_level")
    private Integer experienceLevel;

    @DecimalMin("0.00")
    @DecimalMax("5.00")
    @Column(name = "rating", precision = 3, scale = 2)
    private BigDecimal rating;
}
