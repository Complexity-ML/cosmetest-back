package com.example.cosmetest;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.cosmetest.data.repository.EtudeVolontaireRepository;

@SpringBootTest
class CosmetestApplicationTests {

    @Mock
    private EtudeVolontaireRepository repository;

    @Test
    void contextLoads() {
    }
}
