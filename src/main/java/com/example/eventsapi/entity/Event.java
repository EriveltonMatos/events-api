package com.example.eventsapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "O título é obrigatório")
    @Size(max = 100, message = "O título deve ter 100 caracteres")
    @Column(nullable = false)
    private String titulo;

    @NotNull(message = "Data e hora são obrigatórias")
    @Future(message = "Data e hora devem ser no presente ou futuro")
    @Column(nullable = false)
    private LocalDateTime dataHora;

    @NotBlank(message = "Local é obrigatório")
    @Size(max = 200, message = "Local deve ter no máximo 200 caracteres")
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
