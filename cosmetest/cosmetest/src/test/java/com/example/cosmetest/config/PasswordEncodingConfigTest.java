package com.example.cosmetest.config;

import com.example.cosmetest.security.MD5PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordEncodingConfigTest {
    @Test
    void accepteUnHashMd5HistoriqueEtEncodeLesNouveauxMotsDePasseEnBCrypt() {
        PasswordEncoder encoder = new SecurityConfig(null, null, null).passwordEncoder();
        String legacyMd5 = new MD5PasswordEncoder().encode("secret123");

        assertThat(encoder.matches("secret123", legacyMd5)).isTrue();
        assertThat(encoder.upgradeEncoding(legacyMd5)).isTrue();

        String modern = encoder.encode("secret123");
        assertThat(modern).startsWith("{bcrypt}");
        assertThat(encoder.matches("secret123", modern)).isTrue();
        assertThat(encoder.upgradeEncoding(modern)).isFalse();
    }
}
