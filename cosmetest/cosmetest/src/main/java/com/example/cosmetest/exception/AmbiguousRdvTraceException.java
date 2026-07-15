package com.example.cosmetest.exception;

public class AmbiguousRdvTraceException extends RuntimeException {

    public AmbiguousRdvTraceException(Integer volunteerId, Integer studyId, int count) {
        super("Plusieurs rendez-vous peuvent être associés à l'annulation du volontaire "
                + volunteerId + " dans l'étude " + studyId
                + " (" + count + " rendez-vous). L'identifiant du rendez-vous est obligatoire.");
    }
}
