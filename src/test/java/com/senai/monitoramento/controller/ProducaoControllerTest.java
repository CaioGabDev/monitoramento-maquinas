package com.senai.monitoramento.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProducaoController.class)
// Cada teste recebe uma copia nova do controller (lista em memoria limpa)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProducaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Teste 1 - Consultar dados de producao (RF04)
    @Test
    void testarListarProducao() throws Exception {

        mockMvc.perform(get("/producao"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].maquinaId").value(1))
                .andExpect(jsonPath("$[0].consumoKw").value(12.5))
                .andExpect(jsonPath("$[0].horarioAcionamento").value("08:30"))
                .andExpect(jsonPath("$[0].pecasProduzidas").value(150));
    }

    // Teste 2 - Registrar dados de producao (RF02)
    @Test
    void testarRegistrarProducao() throws Exception {

        String producao = """
                {
                    "maquinaId": 1,
                    "consumoKw": 12.5,
                    "horarioAcionamento": "08:30",
                    "pecasProduzidas": 150
                }
                """;

        mockMvc.perform(post("/producao")
                .contentType("application/json")
                .content(producao))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.consumoKw").value(12.5))
                .andExpect(jsonPath("$.pecasProduzidas").value(150));
    }

    // Teste 3 - Registro invalido deve ser recusado
    @Test
    void testarRegistroInvalido() throws Exception {

        String producao = """
                {
                    "maquinaId": 0,
                    "consumoKw": -5,
                    "horarioAcionamento": "08:30",
                    "pecasProduzidas": -1
                }
                """;

        mockMvc.perform(post("/producao")
                .contentType("application/json")
                .content(producao))
                .andExpect(status().isBadRequest());
    }

    // Teste 4 - Historico de producao e consumo da maquina
    @Test
    void testarHistoricoDaMaquina() throws Exception {

        mockMvc.perform(get("/producao/maquina/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.maquinaId").value(1))
                .andExpect(jsonPath("$.totalConsumoKw").value(22.7))
                .andExpect(jsonPath("$.totalPecasProduzidas").value(270))
                .andExpect(jsonPath("$.historico.length()").value(2));
    }

    // Teste 5 - Historico de maquina sem registros
    @Test
    void testarHistoricoInexistente() throws Exception {

        mockMvc.perform(get("/producao/maquina/99"))
                .andExpect(status().isNotFound());
    }
}
