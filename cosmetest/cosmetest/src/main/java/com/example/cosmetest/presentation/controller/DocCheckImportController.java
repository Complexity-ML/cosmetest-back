package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.dto.DocCheckImportPreviewDTO;
import com.example.cosmetest.business.dto.DocCheckImportRequestDTO;
import com.example.cosmetest.business.dto.VolontaireDetailDTO;
import com.example.cosmetest.business.service.DocCheckImportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/volontaires/import")
public class DocCheckImportController {
    private final DocCheckImportService docCheckImportService;

    public DocCheckImportController(DocCheckImportService docCheckImportService) {
        this.docCheckImportService = docCheckImportService;
    }

    @PostMapping("/preview")
    @Transactional(readOnly = true)
    public ResponseEntity<DocCheckImportPreviewDTO> preview(@RequestBody DocCheckImportRequestDTO request) {
        return ResponseEntity.ok(docCheckImportService.preview(request));
    }

    @PostMapping("/confirm")
    @Transactional
    public ResponseEntity<VolontaireDetailDTO> confirm(@RequestBody DocCheckImportRequestDTO request) {
        try {
            VolontaireDetailDTO created = docCheckImportService.confirm(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
