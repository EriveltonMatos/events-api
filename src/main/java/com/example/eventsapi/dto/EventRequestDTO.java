package com.example.eventsapi.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDTO {
        @NotBlank(message = "O título é obrigatório")
        @Size(max = 100, message = "O título deve ter no máximo 100 caracteres")
        private String titulo;

        @NotNull(message = "Data e hora são obrigatórias")
        @Future(message = "Data e hora devem ser no futuro")
        private LocalDateTime dataHora;

        @NotBlank(message = "Local é obrigatório")
        @Size(max = 200, message = "Local deve ter no máximo 200 caracteres")
        private String local;
}