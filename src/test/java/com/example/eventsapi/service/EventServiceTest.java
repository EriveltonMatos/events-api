package com.example.eventsapi.service;

import com.example.eventsapi.dto.EventRequestDTO;
import com.example.eventsapi.dto.EventResponseDTO;
import com.example.eventsapi.entity.Event;
import com.example.eventsapi.exception.EventNotFoundException;
import com.example.eventsapi.mapper.EventMapper;
import com.example.eventsapi.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EventService Tests")
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    private EventService eventService;

    @BeforeEach
    void setUp() {
        eventService = new EventService(eventRepository, eventMapper);
    }

    @Test
    @DisplayName("findAll() deve retornar lista de eventos quando existirem eventos")
    void findAll_whenEventsExist_shouldReturnEventList() {
        // Arrange
        Event event1 = createEvent(1L, "Evento 1");
        Event event2 = createEvent(2L, "Evento 2");
        List<Event> events = Arrays.asList(event1, event2);

        EventResponseDTO responseDTO1 = createEventResponseDTO(1L, "Evento 1");
        EventResponseDTO responseDTO2 = createEventResponseDTO(2L, "Evento 2");

        when(eventRepository.findByDeletedFalse()).thenReturn(events);
        when(eventMapper.toResponseDTO(event1)).thenReturn(responseDTO1);
        when(eventMapper.toResponseDTO(event2)).thenReturn(responseDTO2);

        // Act
        List<EventResponseDTO> result = eventService.findAll();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitulo()).isEqualTo("Evento 1");
        assertThat(result.get(1).getTitulo()).isEqualTo("Evento 2");

        verify(eventRepository).findByDeletedFalse();
        verify(eventMapper, times(2)).toResponseDTO(any(Event.class));
    }

    @Test
    @DisplayName("findAll(pageable) deve retornar página de eventos")
    void findAllPageable_whenEventsExist_shouldReturnPagedEvents() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Event event = createEvent(1L, "Evento 1");
        Page<Event> eventPage = new PageImpl<>(Arrays.asList(event), pageable, 1);
        EventResponseDTO responseDTO = createEventResponseDTO(1L, "Evento 1");

        when(eventRepository.findByDeletedFalse(pageable)).thenReturn(eventPage);
        when(eventMapper.toResponseDTO(event)).thenReturn(responseDTO);

        // Act
        Page<EventResponseDTO> result = eventService.findAll(pageable);

        // Assert
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitulo()).isEqualTo("Evento 1");
        assertThat(result.getTotalElements()).isEqualTo(1);

        verify(eventRepository).findByDeletedFalse(pageable);
    }

    @Test
    @DisplayName("findById() deve retornar evento quando ID existir")
    void findById_whenEventExists_shouldReturnEvent() {
        // Arrange
        Long eventId = 1L;
        Event event = createEvent(eventId, "Evento Teste");
        EventResponseDTO responseDTO = createEventResponseDTO(eventId, "Evento Teste");

        when(eventRepository.findByIdAndDeletedFalse(eventId)).thenReturn(Optional.of(event));
        when(eventMapper.toResponseDTO(event)).thenReturn(responseDTO);

        // Act
        EventResponseDTO result = eventService.findById(eventId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(eventId);
        assertThat(result.getTitulo()).isEqualTo("Evento Teste");

        verify(eventRepository).findByIdAndDeletedFalse(eventId);
        verify(eventMapper).toResponseDTO(event);
    }

    @Test
    @DisplayName("findById() deve lançar exceção quando ID não existir")
    void findById_whenEventNotExists_shouldThrowException() {
        // Arrange
        Long eventId = 999L;

        when(eventRepository.findByIdAndDeletedFalse(eventId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> eventService.findById(eventId))
                .isInstanceOf(EventNotFoundException.class);

        verify(eventRepository).findByIdAndDeletedFalse(eventId);
        verifyNoInteractions(eventMapper);
    }

    @Test
    @DisplayName("create() deve criar e retornar novo evento")
    void create_whenValidData_shouldCreateAndReturnEvent() {
        // Arrange
        EventRequestDTO requestDTO = createEventRequestDTO("Novo Evento");
        Event event = createEvent(null, "Novo Evento");
        Event savedEvent = createEvent(1L, "Novo Evento");
        EventResponseDTO responseDTO = createEventResponseDTO(1L, "Novo Evento");

        when(eventMapper.toEntity(requestDTO)).thenReturn(event);
        when(eventRepository.save(event)).thenReturn(savedEvent);
        when(eventMapper.toResponseDTO(savedEvent)).thenReturn(responseDTO);

        // Act
        EventResponseDTO result = eventService.create(requestDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitulo()).isEqualTo("Novo Evento");

        verify(eventMapper).toEntity(requestDTO);
        verify(eventRepository).save(event);
        verify(eventMapper).toResponseDTO(savedEvent);
    }

    @Test
    @DisplayName("update() deve atualizar evento existente")
    void update_whenEventExists_shouldUpdateEvent() {
        // Arrange
        Long eventId = 1L;
        EventRequestDTO requestDTO = createEventRequestDTO("Evento Atualizado");
        Event existingEvent = createEvent(eventId, "Evento Original");
        Event updatedEvent = createEvent(eventId, "Evento Atualizado");
        EventResponseDTO responseDTO = createEventResponseDTO(eventId, "Evento Atualizado");

        when(eventRepository.findByIdAndDeletedFalse(eventId)).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(existingEvent)).thenReturn(updatedEvent);
        when(eventMapper.toResponseDTO(updatedEvent)).thenReturn(responseDTO);

        // Act
        EventResponseDTO result = eventService.update(eventId, requestDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(eventId);
        assertThat(result.getTitulo()).isEqualTo("Evento Atualizado");

        verify(eventRepository).findByIdAndDeletedFalse(eventId);
        verify(eventMapper).updateEntity(existingEvent, requestDTO);
        verify(eventRepository).save(existingEvent);
        verify(eventMapper).toResponseDTO(updatedEvent);
    }

    @Test
    @DisplayName("update() deve lançar exceção quando evento não existir")
    void update_whenEventNotExists_shouldThrowException() {
        // Arrange
        Long eventId = 999L;
        EventRequestDTO requestDTO = createEventRequestDTO("Evento Atualizado");

        when(eventRepository.findByIdAndDeletedFalse(eventId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> eventService.update(eventId, requestDTO))
                .isInstanceOf(EventNotFoundException.class);

        verify(eventRepository).findByIdAndDeletedFalse(eventId);
        verifyNoInteractions(eventMapper);
        verify(eventRepository, never()).save(any());
    }

    @Test
    @DisplayName("delete() deve fazer soft delete do evento")
    void delete_whenEventExists_shouldSoftDeleteEvent() {
        // Arrange
        Long eventId = 1L;
        Event event = createEvent(eventId, "Evento a ser deletado");
        event.setDeleted(false);

        when(eventRepository.findByIdAndDeletedFalse(eventId)).thenReturn(Optional.of(event));

        // Act
        eventService.delete(eventId);

        // Assert
        assertThat(event.isDeleted()).isTrue();

        verify(eventRepository).findByIdAndDeletedFalse(eventId);
        verify(eventRepository).save(event);
    }

    @Test
    @DisplayName("delete() deve lançar exceção quando evento não existir")
    void delete_whenEventNotExists_shouldThrowException() {
        // Arrange
        Long eventId = 999L;

        when(eventRepository.findByIdAndDeletedFalse(eventId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> eventService.delete(eventId))
                .isInstanceOf(EventNotFoundException.class);

        verify(eventRepository).findByIdAndDeletedFalse(eventId);
        verify(eventRepository, never()).save(any());
    }

    // Métodos auxiliares para criar objetos de teste
    private Event createEvent(Long id, String titulo) {
        Event event = new Event();
        event.setId(id);
        event.setTitulo(titulo);
        event.setDataHora(LocalDateTime.now().plusDays(1));
        event.setLocal("Local Teste");
        event.setDeleted(false);
        return event;
    }

    private EventResponseDTO createEventResponseDTO(Long id, String titulo) {
        return new EventResponseDTO(id, titulo, LocalDateTime.now().plusDays(1), "Local Teste", false);
    }

    private EventRequestDTO createEventRequestDTO(String titulo) {
        return new EventRequestDTO(titulo, LocalDateTime.now().plusDays(1), "Local Teste");
    }
}