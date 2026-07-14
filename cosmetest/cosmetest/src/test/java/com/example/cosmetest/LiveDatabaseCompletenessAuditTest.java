package com.example.cosmetest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LiveDatabaseCompletenessAuditTest {

    record TechnicalTable(String table, String id, List<String> required) {}

    @Test
    @EnabledIfEnvironmentVariable(named = "RUN_LIVE_DB_AUDIT", matches = "true")
    void auditsTechnicalKeysCompletenessAndReferentialAnomaliesReadOnly() throws Exception {
        String url = System.getenv().getOrDefault("DB_URL",
                "jdbc:mysql://localhost:3306/cosmetest_java?zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
        String user = System.getenv().getOrDefault("DB_USERNAME", "root");
        String password = System.getenv("DB_PASSWORD");
        assertThat(password).as("DB_PASSWORD").isNotNull();

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            connection.setReadOnly(true);
            connection.setAutoCommit(false);

            List<TechnicalTable> migrations = List.of(
                    new TechnicalTable("etude_volontaire", "ID_ETUDE_VOLONTAIRE", List.of("ID_ETUDE", "ID_GROUPE", "ID_VOLONTAIRE")),
                    new TechnicalTable("rdv", "RDV_PK", List.of("ID_ETUDE", "ID_RDV")),
                    new TechnicalTable("infobancaire", "ID_INFOBANCAIRE", List.of("BIC", "IBAN", "ID_VOL")),
                    new TechnicalTable("preetudevol", "ID_PREETUDEVOL", List.of("ID_ETUDE", "ID_GROUPE", "ID_VOLONTAIRE"))
            );

            for (TechnicalTable migration : migrations) {
                long tableExists = scalar(connection, "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema=DATABASE() AND table_name=?", migration.table());
                long columnExists = scalar(connection, "SELECT COUNT(*) FROM information_schema.columns WHERE table_schema=DATABASE() AND table_name=? AND column_name=?", migration.table(), migration.id());
                long primaryKey = scalar(connection, "SELECT COUNT(*) FROM information_schema.key_column_usage WHERE table_schema=DATABASE() AND table_name=? AND constraint_name='PRIMARY' AND column_name=?", migration.table(), migration.id());
                long autoIncrement = scalar(connection, "SELECT COUNT(*) FROM information_schema.columns WHERE table_schema=DATABASE() AND table_name=? AND column_name=? AND LOWER(extra) LIKE '%auto_increment%'", migration.table(), migration.id());

                assertThat(tableExists).as(migration.table() + " exists").isEqualTo(1);
                assertThat(columnExists).as(migration.table() + " technical column").isEqualTo(1);
                assertThat(primaryKey).as(migration.table() + " technical PK").isEqualTo(1);
                assertThat(autoIncrement).as(migration.table() + " auto increment").isEqualTo(1);

                String table = quote(migration.table());
                String id = quote(migration.id());
                long rows = scalar(connection, "SELECT COUNT(*) FROM " + table);
                long nullIds = scalar(connection, "SELECT COUNT(*) FROM " + table + " WHERE " + id + " IS NULL");
                long duplicateIds = scalar(connection, "SELECT COUNT(*) FROM (SELECT " + id + " FROM " + table + " GROUP BY " + id + " HAVING COUNT(*) > 1) d");
                long missingRequired = scalar(connection, "SELECT COUNT(*) FROM " + table + " WHERE " + String.join(" OR ", migration.required().stream().map(c -> quote(c) + " IS NULL").toList()));

                System.out.printf("LIVE_DB_TABLE table=%s rows=%d nullTechnicalIds=%d duplicateTechnicalIds=%d missingRequired=%d%n",
                        migration.table(), rows, nullIds, duplicateIds, missingRequired);
                assertThat(nullIds).as(migration.table() + " null technical IDs").isZero();
                assertThat(duplicateIds).as(migration.table() + " duplicate technical IDs").isZero();
                assertThat(missingRequired).as(migration.table() + " missing required values").isZero();
            }

            report(connection, "etude_volontaire_exact7_duplicates", """
                    SELECT COUNT(*) FROM (
                      SELECT 1 FROM etude_volontaire
                      GROUP BY ID_ETUDE, ID_GROUPE, ID_VOLONTAIRE, IV, NUMSUJET, PAYE, STATUT
                      HAVING COUNT(*) > 1
                    ) d
                    """);
            report(connection, "etude_volontaire_legacy_triplets_ambiguous", """
                    SELECT COUNT(*) FROM (
                      SELECT 1 FROM etude_volontaire
                      GROUP BY ID_ETUDE, ID_GROUPE, ID_VOLONTAIRE
                      HAVING COUNT(*) > 1
                    ) d
                    """);
            report(connection, "rdv_business_key_duplicates", """
                    SELECT COUNT(*) FROM (
                      SELECT 1 FROM rdv GROUP BY ID_ETUDE, ID_RDV HAVING COUNT(*) > 1
                    ) d
                    """);
            report(connection, "preetudevol_business_triplet_duplicates", """
                    SELECT COUNT(*) FROM (
                      SELECT 1 FROM preetudevol GROUP BY ID_ETUDE, ID_GROUPE, ID_VOLONTAIRE HAVING COUNT(*) > 1
                    ) d
                    """);
            report(connection, "infobancaire_business_duplicates", """
                    SELECT COUNT(*) FROM (
                      SELECT 1 FROM infobancaire GROUP BY BIC, IBAN, ID_VOL HAVING COUNT(*) > 1
                    ) d
                    """);

            report(connection, "orphan_etude_volontaire_etude", "SELECT COUNT(*) FROM etude_volontaire x LEFT JOIN etude e ON e.ID_ETUDE=x.ID_ETUDE WHERE e.ID_ETUDE IS NULL");
            report(connection, "orphan_etude_volontaire_volontaire", "SELECT COUNT(*) FROM etude_volontaire x LEFT JOIN volontaire v ON v.ID_VOL=x.ID_VOLONTAIRE WHERE v.ID_VOL IS NULL");
            report(connection, "orphan_etude_volontaire_groupe", "SELECT COUNT(*) FROM etude_volontaire x LEFT JOIN groupe g ON g.ID_GROUPE=x.ID_GROUPE WHERE g.ID_GROUPE IS NULL");
            report(connection, "orphan_rdv_etude", "SELECT COUNT(*) FROM rdv x LEFT JOIN etude e ON e.ID_ETUDE=x.ID_ETUDE WHERE e.ID_ETUDE IS NULL");
            report(connection, "orphan_rdv_volontaire", "SELECT COUNT(*) FROM rdv x LEFT JOIN volontaire v ON v.ID_VOL=x.ID_VOLONTAIRE WHERE x.ID_VOLONTAIRE IS NOT NULL AND v.ID_VOL IS NULL");
            report(connection, "orphan_infobancaire_volontaire", "SELECT COUNT(*) FROM infobancaire x LEFT JOIN volontaire v ON v.ID_VOL=x.ID_VOL WHERE v.ID_VOL IS NULL");
            report(connection, "orphan_preetudevol_etude", "SELECT COUNT(*) FROM preetudevol x LEFT JOIN etude e ON e.ID_ETUDE=x.ID_ETUDE WHERE e.ID_ETUDE IS NULL");
            report(connection, "orphan_preetudevol_volontaire", "SELECT COUNT(*) FROM preetudevol x LEFT JOIN volontaire v ON v.ID_VOL=x.ID_VOLONTAIRE WHERE v.ID_VOL IS NULL");
            report(connection, "orphan_preetudevol_groupe", "SELECT COUNT(*) FROM preetudevol x LEFT JOIN groupe g ON g.ID_GROUPE=x.ID_GROUPE WHERE g.ID_GROUPE IS NULL");

            reportRows(connection, "orphan_infobancaire_ids", """
                    SELECT x.ID_VOL, COUNT(*)
                    FROM infobancaire x LEFT JOIN volontaire v ON v.ID_VOL=x.ID_VOL
                    WHERE v.ID_VOL IS NULL
                    GROUP BY x.ID_VOL ORDER BY COUNT(*) DESC, x.ID_VOL LIMIT 20
                    """);
            reportRows(connection, "orphan_preetudevol_keys", """
                    SELECT x.ID_ETUDE, x.ID_GROUPE, x.ID_VOLONTAIRE, COUNT(*)
                    FROM preetudevol x
                    LEFT JOIN etude e ON e.ID_ETUDE=x.ID_ETUDE
                    LEFT JOIN groupe g ON g.ID_GROUPE=x.ID_GROUPE
                    LEFT JOIN volontaire v ON v.ID_VOL=x.ID_VOLONTAIRE
                    WHERE e.ID_ETUDE IS NULL OR g.ID_GROUPE IS NULL OR v.ID_VOL IS NULL
                    GROUP BY x.ID_ETUDE, x.ID_GROUPE, x.ID_VOLONTAIRE
                    ORDER BY COUNT(*) DESC, x.ID_ETUDE, x.ID_GROUPE, x.ID_VOLONTAIRE LIMIT 30
                    """);

            connection.rollback();
        }
    }

    private static void report(Connection connection, String label, String sql) throws Exception {
        System.out.printf("LIVE_DB_CHECK check=%s count=%d%n", label, scalar(connection, sql));
    }

    private static void reportRows(Connection connection, String label, String sql) throws Exception {
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                StringBuilder row = new StringBuilder();
                for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
                    if (i > 1) row.append(',');
                    row.append(result.getObject(i));
                }
                System.out.printf("LIVE_DB_DETAIL check=%s values=%s%n", label, row);
            }
        }
    }

    private static long scalar(Connection connection, String sql, Object... parameters) throws Exception {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.length; i++) statement.setObject(i + 1, parameters[i]);
            try (ResultSet result = statement.executeQuery()) {
                assertThat(result.next()).isTrue();
                return result.getLong(1);
            }
        }
    }

    private static String quote(String identifier) {
        return "`" + identifier.replace("`", "``") + "`";
    }
}
