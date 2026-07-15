package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.service.VolontaireQuestionnaireService;
import com.example.cosmetest.presentation.request.VolontaireQuestionnaireRequest;
import com.example.cosmetest.presentation.response.VolontaireQuestionnaireResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/volontaires")
public class VolontaireQuestionnaireController {

    private final VolontaireQuestionnaireService questionnaireService;

    public VolontaireQuestionnaireController(VolontaireQuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    @PutMapping("/{id}/questionnaire")
    public ResponseEntity<VolontaireQuestionnaireResponse> save(
            @PathVariable @Positive Integer id,
            @Valid @RequestBody VolontaireQuestionnaireRequest request) {
        return ResponseEntity.ok(questionnaireService.save(id, request));
    }
}