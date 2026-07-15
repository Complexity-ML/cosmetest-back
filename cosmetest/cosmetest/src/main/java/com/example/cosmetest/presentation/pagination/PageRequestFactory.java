package com.example.cosmetest.presentation.pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Locale;
import java.util.Set;

public final class PageRequestFactory {

    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;

    private PageRequestFactory() {
    }

    public static Pageable create(
            int page,
            int size,
            String sortBy,
            String direction,
            Set<String> allowedSortProperties) {
        if (allowedSortProperties == null || !allowedSortProperties.contains(sortBy)) {
            throw new IllegalArgumentException("Champ de tri non autorisé: " + sortBy);
        }

        Sort.Direction sortDirection;
        try {
            sortDirection = Sort.Direction.valueOf(direction.toUpperCase(Locale.ROOT));
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("Direction de tri invalide: " + direction);
        }

        int safePage = Math.max(0, page);
        int requestedSize = size > 0 ? size : DEFAULT_PAGE_SIZE;
        int safeSize = Math.min(MAX_PAGE_SIZE, requestedSize);
        return PageRequest.of(safePage, safeSize, Sort.by(sortDirection, sortBy));
    }

    public static Pageable create(int page, int size) {
        int safePage = Math.max(0, page);
        int requestedSize = size > 0 ? size : DEFAULT_PAGE_SIZE;
        return PageRequest.of(safePage, Math.min(MAX_PAGE_SIZE, requestedSize));
    }
}
