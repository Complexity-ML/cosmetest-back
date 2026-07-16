package com.example.cosmetest.data.repository;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.Query;

import static org.assertj.core.api.Assertions.assertThat;

class EtudeVolontairePaymentSummaryQueryTest {

    @Test
    void paymentSummaryUsesOneCanonicalAssociationPerVolunteer() throws Exception {
        Query query = EtudeVolontaireRepository.class
                .getMethod("fetchEtudePaiementSummaries", Integer.class)
                .getAnnotation(Query.class);

        assertThat(query).isNotNull();
        assertThat(query.value())
                .contains("GROUP_CONCAT(id_etude_volontaire")
                .contains("GROUP BY id_etude,id_volontaire")
                .contains("canonical.id_etude_volontaire=ev.id_etude_volontaire");
    }

    @Test
    void dashboardCountUsesDistinctVolunteersAndExcludesCancellations() throws Exception {
        Query query = EtudeVolontaireRepository.class
                .getMethod("countActiveDistinctVolunteersByStudyIds", java.util.List.class)
                .getAnnotation(Query.class);

        assertThat(query).isNotNull();
        assertThat(query.value().toLowerCase())
                .contains("count(distinct ev.idvolontaire)")
                .contains("ev.idetude in :idetudes")
                .contains("not exists")
                .contains("a.idvol = ev.idvolontaire");
    }
}
