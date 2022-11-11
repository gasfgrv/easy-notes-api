package com.gsilva.easynotes.controller;

import com.gsilva.easynotes.dto.NoteDetailsDto;
import com.gsilva.easynotes.dto.NoteDto;
import com.gsilva.easynotes.dto.NoteForm;
import com.gsilva.easynotes.model.Note;
import com.gsilva.easynotes.service.NotesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
public class NotesController {

    private final ModelMapper modelMapper;
    private final NotesService notesService;

    @Autowired
    public NotesController(ModelMapper modelMapper, NotesService notesService) {
        this.modelMapper = modelMapper;
        this.notesService = notesService;
    }

    @GetMapping("/notes")
    public ResponseEntity<List<NoteDto>> getAllNotes() {
        List<NoteDto> dtoList = notesService.getAllNotes().stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    @PostMapping("/notes")
    public ResponseEntity<NoteDto> createNote(@Valid @RequestBody NoteForm form) {
        Note note = notesService.createNote(toModel(form));
        return ResponseEntity.ok(toDto(note));
    }

    @GetMapping("/notes/{id}")
    public ResponseEntity<NoteDetailsDto> getNoteById(@PathVariable(value = "id") Long id) {
        return notesService.getNoteById(id)
                .map(value -> ResponseEntity.ok(toDetails(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/notes/{id}")
    public ResponseEntity<NoteDto> updateNote(@PathVariable(value = "id") Long id, @Valid @RequestBody NoteForm form) {
        return ResponseEntity.ok(toDto(notesService.updateNote(id, form)));
    }

    @DeleteMapping("/notes/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable(value = "id") Long id) {
        notesService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }

    private NoteDto toDto(Note note) {
        NoteDto dto = modelMapper.map(note, NoteDto.class);
        dto.add(linkTo(methodOn(NotesController.class).getNoteById(note.getId())).withSelfRel().withType("GET"));
        return dto;
    }

    private NoteDetailsDto toDetails(Note note) {
        NoteDetailsDto dto = modelMapper.map(note, NoteDetailsDto.class);
        dto.add(linkTo(methodOn(NotesController.class).getAllNotes()).withRel("all-notes-get").withType("GET"));
        dto.add(linkTo(methodOn(NotesController.class).updateNote(note.getId(), new NoteForm())).withRel("notes-put").withType("PUT"));
        dto.add(linkTo(methodOn(NotesController.class).deleteNote(note.getId())).withRel("notes-delete").withType("DELETE"));
        return dto;
    }

    private Note toModel(NoteForm form) {
        return modelMapper.map(form, Note.class);
    }

}
