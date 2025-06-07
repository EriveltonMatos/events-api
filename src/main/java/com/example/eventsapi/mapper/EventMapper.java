
package com.example.eventsapi.mapper;

import com.example.eventsapi.dto.EventRequestDTO;
import com.example.eventsapi.dto.EventResponseDTO;
import com.example.eventsapi.entity.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public Event toEntity(EventRequestDTO dto) {
        return new Event(dto.getTitulo(), dto.getDataHora(), dto.getLocal());
    }

    public EventResponseDTO toResponseDTO(Event entity) {
        return new EventResponseDTO(
                entity.getId(),
                entity.getTitulo(),
                entity.getDataHora(),
                entity.getLocal(),
                entity.isDeleted()
        );
    }

    public void updateEntity(Event entity, EventRequestDTO dto) {
        entity.setTitulo(dto.getTitulo());
        entity.setDataHora(dto.getDataHora());
        entity.setLocal(dto.getLocal());
    }
}