package com.example.cosmetest.data.repository;

import com.example.cosmetest.domain.model.Infobancaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InfobancaireRepository extends JpaRepository<Infobancaire, Long> {
    Optional<Infobancaire> findByBicAndIbanAndIdVol(String bic, String iban, Integer idVol);
    boolean existsByBicAndIbanAndIdVol(String bic, String iban, Integer idVol);
    List<Infobancaire> findByIdVol(Integer idVol);
    boolean existsByIdVol(Integer idVol);
    boolean existsByIban(String iban);
    List<Infobancaire> findByIban(String iban);
    List<Infobancaire> findByBic(String bic);
    List<Infobancaire> findByBicAndIban(String bic, String iban);
}
