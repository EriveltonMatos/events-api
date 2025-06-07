package com.example.eventsapi.controller;

import com.example.eventsapi.dto.EventRequestDTO;
import com.example.eventsapi.dto.EventResponseDTO;
import com.example.eventsapi.dto.PageResponseDTO;
import com.example.eventsapi.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    @Operation(summary = "Lista todos os eventos", description = "Retorna uma lista de todos os eventos que não foram deletados")
    public ResponseEntity<PageResponseDTO<EventResponseDTO>> findAll(
            @PageableDefault(size = 10) Pageable pageable) {

        Page<EventResponseDTO> events = eventService.findAll(pageable);
        PageResponseDTO<EventResponseDTO> response = PageResponseDTO.of(events);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(summary = "Lista todos os eventos sem paginação", description = "Retorna uma lista completa de todos os eventos")
    public ResponseEntity<List<EventResponseDTO>> findAllWithoutPagination() {
        List<EventResponseDTO> events = eventService.findAll();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca evento por ID", description = "Retorna os detalhes de um evento específico")
    public ResponseEntity<EventResponseDTO> findById(@PathVariable Long id) {
        EventResponseDTO event = eventService.findById(id);
        return ResponseEntity.ok(event);
    }

    @PostMapping
    @Operation(summary = "Cria um novo evento", description = "Cria um novo evento no sistema")
    public ResponseEntity<EventResponseDTO> create(@Valid @RequestBody EventRequestDTO eventDTO) {
        EventResponseDTO createdEvent = eventService.create(eventDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza evento", description = "Atualiza os dados de um evento")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody EventRequestDTO eventDTO) {
        eventService.update(id, eventDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove evento", description = "Remove um evento do sistema")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}