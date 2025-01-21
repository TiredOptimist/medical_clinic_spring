package ru.medicalclinic.medical_clinic.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "medications")
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medication_id", nullable = false)
    private Long id;

    @Pattern(regexp = "^[a-zA-Zа-яА-Я0-9\\s.,!?\"'()-]+$", message = "Diagnosis must contain only letters, digits, and punctuation")
    @Column(name = "diagnosis", length = Integer.MAX_VALUE)
    private String diagnosis;

    @NotNull
    @Pattern(regexp = "^[a-zA-Zа-яА-Я0-9\\s.,!?\"'()-]+$", message = "Name must contain only letters, digits, and punctuation")
    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @NotNull
    @Pattern(regexp = "^[a-zA-Zа-яА-Я0-9\\s.,!?\"'()-]+$", message = "Dosage must contain only letters, digits, and punctuation")
    @Column(name = "dosage", nullable = false, length = Integer.MAX_VALUE)
    private String dosage;

    @NotNull
    @Column(name = "visit_id", nullable = false)
    private Long visitId;
}
