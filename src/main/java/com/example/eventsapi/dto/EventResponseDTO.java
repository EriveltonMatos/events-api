package com.example.eventsapi.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventResponseDTO {
    private Long id;
    private String titulo;
    private LocalDateTime dataHora;
    private String local;
    private boolean deleted;
}