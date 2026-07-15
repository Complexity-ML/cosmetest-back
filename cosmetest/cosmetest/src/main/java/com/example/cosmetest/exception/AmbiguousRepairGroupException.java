package com.example.cosmetest.exception;

public class AmbiguousRepairGroupException extends RuntimeException {

    public AmbiguousRepairGroupException(Integer volunteerId, int studyId, int count) {
        super("Impossible de réparer l'association du volontaire " + volunteerId
                + " dans l'étude " + studyId + ": " + count
                + " groupes candidats distincts");
    }
}
