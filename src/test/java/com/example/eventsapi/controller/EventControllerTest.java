package com.example.eventsapi.controller;

import com.example.eventsapi.dto.EventRequestDTO;
import com.example.eventsapi.dto.EventResponseDTO;
import com.example.eventsapi.exception.EventNotFoundException;
import com.example.eventsapi.exception.GlobalExceptionHandler;
import com.example.eventsapi.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EventController Tests")
class EventControllerTest {

    @Mock
    private EventService eventService;

    private EventController eventController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        eventController = new EventController(eventService);

        mockMvc = MockMvcBuilders.standaloneSetup(eventController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @DisplayName("GET /api/events/all deve retornar lista completa de eventos")
    void findAllWithoutPagination_whenCalled_shouldReturnAllEvents() throws Exception {
        List<EventResponseDTO> events = Arrays.asList(
                createEventResponseDTO(1L, "Evento 1"),
                createEventResponseDTO(2L, "Evento 2")
        );

        when(eventService.findAll()).thenReturn(events);

        mockMvc.perform(get("/api/events/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Evento 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].titulo").value("Evento 2"));

        verify(eventService).findAll();
    }

    @Test
    @DisplayName("GET /api/events/{id} deve retornar evento quando ID existir")
    void findById_whenEventExists_shouldReturnEvent() throws Exception {
        Long eventId = 1L;
        EventResponseDTO event = createEventResponseDTO(eventId, "Evento Teste");

        when(eventService.findById(eventId)).thenReturn(event);

        mockMvc.perform(get("/api/events/{id}", eventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(eventId))
                .andExpect(jsonPath("$.titulo").value("Evento Teste"));

        verify(eventService).findById(eventId);
    }

    @Test
    @DisplayName("GET /api/events/{id} deve retornar 404 quando evento não existir")
    void findById_whenEventNotExists_shouldReturn404() throws Exception {
        Long eventId = 999L;

        when(eventService.findById(eventId)).thenThrow(new EventNotFoundException(eventId));

        mockMvc.perform(get("/api/events/{id}", eventId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());

        verify(eventService).findById(eventId);
    }

    @Test
    @DisplayName("POST /api/events deve criar evento com dados válidos")
    void create_whenValidData_shouldCreateEvent() throws Exception {
        EventRequestDTO requestDTO = createEventRequestDTO("Novo Evento");
        EventResponseDTO responseDTO = createEventResponseDTO(1L, "Novo Evento");

        when(eventService.create(any(EventRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Novo Evento"));

        verify(eventService).create(any(EventRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/events deve retornar 400 com dados inválidos")
    void create_whenInvalidData_shouldReturn400() throws Exception {
        EventRequestDTO invalidRequest = new EventRequestDTO();
        invalidRequest.setTitulo(""); // Título vazio
        invalidRequest.setDataHora(LocalDateTime.now().minusDays(1)); // Data no passado
        invalidRequest.setLocal(""); // Local vazio

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors").exists());

        verifyNoInteractions(eventService);
    }

    @Test
    @DisplayName("PUT /api/events/{id} deve atualizar evento existente")
    void update_whenEventExists_shouldUpdateEvent() throws Exception {
        Long eventId = 1L;
        EventRequestDTO requestDTO = createEventRequestDTO("Evento Atualizado");
        EventResponseDTO responseDTO = createEventResponseDTO(eventId, "Evento Atualizado");

        when(eventService.update(eq(eventId), any(EventRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/events/{id}", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNoContent());

        verify(eventService).update(eq(eventId), any(EventRequestDTO.class));
    }

    @Test
    @DisplayName("PUT /api/events/{id} deve retornar 404 quando evento não existir")
    void update_whenEventNotExists_shouldReturn404() throws Exception {
        Long eventId = 999L;
        EventRequestDTO requestDTO = createEventRequestDTO("Evento Atualizado");

        when(eventService.update(eq(eventId), any(EventRequestDTO.class)))
                .thenThrow(new EventNotFoundException(eventId));

        mockMvc.perform(put("/api/events/{id}", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").exists());

        verify(eventService).update(eq(eventId), any(EventRequestDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/events/{id} deve deletar evento existente")
    void delete_whenEventExists_shouldDeleteEvent() throws Exception {
        Long eventId = 1L;

        doNothing().when(eventService).delete(eventId);

        mockMvc.perform(delete("/api/events/{id}", eventId))
                .andExpect(status().isNoContent());

        verify(eventService).delete(eventId);
    }

    @Test
    @DisplayName("DELETE /api/events/{id} deve retornar 404 quando evento não existir")
    void delete_whenEventNotExists_shouldReturn404() throws Exception {
        Long eventId = 999L;

        doThrow(new EventNotFoundException(eventId)).when(eventService).delete(eventId);

        mockMvc.perform(delete("/api/events/{id}", eventId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").exists());

        verify(eventService).delete(eventId);
    }

    private EventResponseDTO createEventResponseDTO(Long id, String titulo) {
        return new EventResponseDTO(id, titulo, LocalDateTime.now().plusDays(1), "Local Teste", false);
    }

    private EventRequestDTO createEventRequestDTO(String titulo) {
        return new EventRequestDTO(titulo, LocalDateTime.now().plusDays(1), "Local Teste");
    }
}