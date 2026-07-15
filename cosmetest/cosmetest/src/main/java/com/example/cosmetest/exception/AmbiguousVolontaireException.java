package com.example.cosmetest.exception;

public class AmbiguousVolontaireException extends RuntimeException {

    public AmbiguousVolontaireException(String criterion, int count) {
        super("Plusieurs volontaires correspondent au critère " + criterion
                + " (" + count + " lignes)");
    }
}
