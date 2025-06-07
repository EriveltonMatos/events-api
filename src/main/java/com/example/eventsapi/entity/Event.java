package com.example.eventsapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false, length = 200)
    private String local;

    @Column(nullable = false)
    private boolean deleted = false;

    public Event(String titulo, LocalDateTime dataHora, String local) {
        this.titulo = titulo;
        this.dataHora = dataHora;
        this.local = local;
    }
}