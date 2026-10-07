package ru.student.event_registration.service;

import org.springframework.stereotype.Service;
import ru.student.event_registration.model.Participant;
import ru.student.event_registration.repository.ParticipantRepository;

import java.util.List;

@Service
public class ParticipantService {

    private final ParticipantRepository repository;

    public ParticipantService(ParticipantRepository repository) {
        this.repository = repository;
    }

    public List<Participant> getAllParticipants() {
        return repository.findAll();
    }

    public Participant saveParticipant(Participant participant) {
        return repository.save(participant);
    }

    public List<Participant> searchParticipants(String name) {
        if (name == null || name.trim().isEmpty()) {
            return repository.findAll();
        }
        return repository.findByNameContainingIgnoreCase(name);
    }

    public Participant getParticipantById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Участник не найден"));
    }

    public void deleteParticipantById(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Участник с ID " + id + " не найден");
        }
        repository.deleteById(id);
    }
}
