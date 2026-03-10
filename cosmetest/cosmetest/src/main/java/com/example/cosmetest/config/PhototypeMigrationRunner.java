package com.example.cosmetest.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Migration one-shot : normalise tous les phototypes vers "Phototype 1"-"Phototype 6".
 * Gère : "I", "II", "1", "2", "Phototype III", "Phototype_3", etc.
 */
@Component
public class PhototypeMigrationRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(PhototypeMigrationRunner.class);

    private final JdbcTemplate jdbcTemplate;

    public PhototypeMigrationRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        migrateTable("volontaire", "phototype");
        migrateTable("panel", "phototype");
    }

    private void migrateTable(String table, String column) {
        String[][] mappings = {
            // Romain seul → Phototype X
            {"VI", "Phototype 6"}, {"IV", "Phototype 4"}, {"V", "Phototype 5"},
            {"III", "Phototype 3"}, {"II", "Phototype 2"}, {"I", "Phototype 1"},
            // Arabe seul → Phototype X
            {"1", "Phototype 1"}, {"2", "Phototype 2"}, {"3", "Phototype 3"},
            {"4", "Phototype 4"}, {"5", "Phototype 5"}, {"6", "Phototype 6"},
            // Romain avec préfixe → Phototype X
            {"Phototype VI", "Phototype 6"}, {"Phototype IV", "Phototype 4"}, {"Phototype V", "Phototype 5"},
            {"Phototype III", "Phototype 3"}, {"Phototype II", "Phototype 2"}, {"Phototype I", "Phototype 1"},
            // Format underscore
            {"Phototype_1", "Phototype 1"}, {"Phototype_2", "Phototype 2"}, {"Phototype_3", "Phototype 3"},
            {"Phototype_4", "Phototype 4"}, {"Phototype_5", "Phototype 5"}, {"Phototype_6", "Phototype 6"},
        };

        int totalUpdated = 0;
        for (String[] mapping : mappings) {
            String from = mapping[0];
            String to = mapping[1];
            try {
                int updated = jdbcTemplate.update(
                    "UPDATE " + table + " SET " + column + " = ? WHERE TRIM(" + column + ") = ?",
                    to, from
                );
                if (updated > 0) {
                    log.info("Migration phototype {}.{}: '{}' -> '{}' ({} lignes)", table, column, from, to, updated);
                    totalUpdated += updated;
                }
            } catch (Exception e) {
                log.warn("Migration phototype {}.{} ignorée pour '{}': {}", table, column, from, e.getMessage());
            }
        }
        if (totalUpdated > 0) {
            log.info("Migration phototype {}.{}: {} lignes mises à jour au total", table, column, totalUpdated);
        }
    }
}
