package ru.student.event_registration.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.student.event_registration.model.Participant;
import ru.student.event_registration.service.ParticipantService;

@Controller
public class ParticipantController {

    private final ParticipantService service;

    public ParticipantController(ParticipantService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String listParticipants(Model model) {
        model.addAttribute("participants", service.getAllParticipants());
        model.addAttribute("searchQuery", "");
        return "list";
    }

    @GetMapping("/search")
    public String searchParticipants(@RequestParam("query") String query, Model model) {
        model.addAttribute("participants", service.searchParticipants(query));
        model.addAttribute("searchQuery", query);
        return "list";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("participant", new Participant());
        model.addAttribute("isEdit", false);
        return "form";
    }

    @PostMapping("/register")
    public String processRegisterForm(
            @ModelAttribute @Valid Participant participant,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "form";
        }
        participant.setRegistrationDate(java.time.LocalDateTime.now());
        try {
            service.saveParticipant(participant);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "Такой email уже зарегистрирован");
            model.addAttribute("isEdit", false);
            return "form";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Participant participant = service.getParticipantById(id);
        model.addAttribute("participant", participant);
        model.addAttribute("isEdit", true);
        return "form";
    }

    @PostMapping("/edit/{id}")
    public String processEditForm(
            @PathVariable Long id,
            @ModelAttribute @Valid Participant participant,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", true);
            return "form";
        }

        Participant existing = service.getParticipantById(id);
        participant.setId(id);
        participant.setRegistrationDate(existing.getRegistrationDate());
        try {
            service.saveParticipant(participant);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "Такой email уже зарегистрирован");
            model.addAttribute("isEdit", true);
            return "form";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteParticipant(@PathVariable Long id) {
        service.deleteParticipantById(id);
        return "redirect:/";
    }
}
