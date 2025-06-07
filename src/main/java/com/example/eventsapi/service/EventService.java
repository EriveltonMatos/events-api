package com.example.eventsapi.service;

import com.example.eventsapi.dto.EventRequestDTO;
import com.example.eventsapi.dto.EventResponseDTO;
import com.example.eventsapi.entity.Event;
import com.example.eventsapi.exception.EventNotFoundException;
import com.example.eventsapi.mapper.EventMapper;
import com.example.eventsapi.repository.EventRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public List<EventResponseDTO> findAll() {
        log.info("Buscando todos os eventos");
        List<Event> events = eventRepository.findByDeletedFalse();
        return events.stream()
                .map(eventMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Page<EventResponseDTO> findAll(Pageable pageable) {
        log.info("Buscando eventos paginados - página: {}, tamanho: {}",
                pageable.getPageNumber(), pageable.getPageSize());
        Page<Event> events = eventRepository.findByDeletedFalse(pageable);
        return events.map(eventMapper::toResponseDTO);
    }

    public EventResponseDTO findById(Long id) {
        log.info("Buscando evento com ID: {}", id);
        Event event = eventRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EventNotFoundException(id));
        return eventMapper.toResponseDTO(event);
    }

    @Transactional
    public EventResponseDTO create(EventRequestDTO eventDTO) {
        log.info("Criando novo evento: {}", eventDTO.getTitulo());
        Event event = eventMapper.toEntity(eventDTO);
        Event savedEvent = eventRepository.save(event);
        log.info("Evento criado com sucesso. ID: {}", savedEvent.getId());
        return eventMapper.toResponseDTO(savedEvent);
    }

    @Transactional
    public EventResponseDTO update(Long id, EventRequestDTO eventDTO) {
        log.info("Atualizando evento com ID: {}", id);
        Event event = eventRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EventNotFoundException(id));

        eventMapper.updateEntity(event, eventDTO);
        Event updatedEvent = eventRepository.save(event);
        log.info("Evento atualizado com sucesso. ID: {}", id);
        return eventMapper.toResponseDTO(updatedEvent);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Removendo evento com ID: {}", id);
        Event event = eventRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EventNotFoundException(id));

        event.setDeleted(true);
        eventRepository.save(event);
        log.info("Evento removido com sucesso. ID: {}", id);
    }
}