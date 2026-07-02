package com.example.cosmetest.business.dto;

import java.util.ArrayList;
import java.util.List;

public class DocCheckImportRequestDTO {
    private String documentId;
    private List<DocCheckFieldDTO> fields = new ArrayList<>();

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public List<DocCheckFieldDTO> getFields() {
        return fields;
    }

    public void setFields(List<DocCheckFieldDTO> fields) {
        this.fields = fields;
    }
}
