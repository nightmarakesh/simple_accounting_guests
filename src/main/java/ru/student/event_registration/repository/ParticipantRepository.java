package ru.student.event_registration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.student.event_registration.model.Participant;
import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByNameContainingIgnoreCase(String name);
}

