package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.DocCheckImportPreviewDTO;
import com.example.cosmetest.business.dto.DocCheckImportRequestDTO;
import com.example.cosmetest.business.dto.VolontaireDetailDTO;

public interface DocCheckImportService {
    DocCheckImportPreviewDTO preview(DocCheckImportRequestDTO request);

    VolontaireDetailDTO confirm(DocCheckImportRequestDTO request);
}
