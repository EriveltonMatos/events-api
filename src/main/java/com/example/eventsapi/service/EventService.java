package com.example.eventsapi.service;

import com.example.eventsapi.dto.EventDTO;
import com.example.eventsapi.entity.Event;
import com.example.eventsapi.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class EventService {
    private final EventRepository eventRepository;

    public List<Event> findAll() {
        return eventRepository.findByDeletedFalse();
    }

    public Page<Event> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return eventRepository.findByDeletedFalse(pageable);
    }

    public Optional<Event> findById(Long id) {
        return eventRepository.findByIdAndDeletedFalse(id);
    }

    public Event create(EventDTO eventDTO) {
        Event event = new Event(eventDTO.getTitulo(), eventDTO.getDataHora(), eventDTO.getLocal());
        return eventRepository.save(event);
    }

    public Optional<Event> update(Long id, EventDTO eventDTO) {
        Optional<Event> optionalEvent = eventRepository.findByIdAndDeletedFalse(id);

        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            event.setTitulo(eventDTO.getTitulo());
            event.setDataHora(eventDTO.getDataHora());
            event.setLocal(eventDTO.getLocal());
            return Optional.of(eventRepository.save(event));
        }

        return Optional.empty();
    }

    public boolean delete(Long id) {
        Optional<Event> optionalEvent = eventRepository.findByIdAndDeletedFalse(id);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            event.setDeleted(true);
            eventRepository.save(event);
            return true;
        }
            return false;
        }
    }

