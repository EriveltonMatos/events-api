package com.example.eventsapi.repository;

import com.example.eventsapi.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByDeletedFalse();

    Page<Event> findByDeletedFalse(Pageable pageable);

    Optional<Event> findByIdAndDeletedFalse(Long id);

}
