package com.example.VeterinaryManagementSystem.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "work_day")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class WorkDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "work_date")
    private LocalDate workDay;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = (Long) id;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Doctor getDoctor() {
        return doctor;
    }
}