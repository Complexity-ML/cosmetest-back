package com.example.cosmetest.exception;

public class AmbiguousVolontaireHcException extends RuntimeException {

    public AmbiguousVolontaireHcException(Integer volontaireId, int count) {
        super("Plusieurs habitudes cosmétiques correspondent au volontaire "
                + volontaireId + " (" + count + " lignes)");
    }
}
