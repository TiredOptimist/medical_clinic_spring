package ru.medicalclinic.medical_clinic.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "visits")
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visit_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @NotNull
    @FutureOrPresent
    @Column(name = "visit_date", nullable = false)
    private LocalDate visitDate;

    @Pattern(regexp = "^[a-zA-Zа-яА-Я0-9\\s.,!?\"'()-]+$", message = "Reason for visit must contain only letters, digits, and punctuation")
    @Column(name = "reason_for_visit", length = Integer.MAX_VALUE)
    private String reasonForVisit;

}