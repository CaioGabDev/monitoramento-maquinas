package com.senai.monitoramento.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MaquinaController.class)
// Cada teste recebe uma copia nova do controller (lista em memoria limpa)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MaquinaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Teste 1 - Listar maquinas cadastradas (RF03)
    @Test
    void testarListarMaquinas() throws Exception {

        mockMvc.perform(get("/maquinas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].nome").value("Maquina CNC 01"))
                .andExpect(jsonPath("$[0].tipo").value("CNC"))
                .andExpect(jsonPath("$[0].setor").value("Usinagem"))
                .andExpect(jsonPath("$[0].status").value("em operacao"));
    }

    // Teste 2 - Cadastrar maquina (RF01)
    @Test
    void testarCadastrarMaquina() throws Exception {

        String maquina = """
                {
                    "nome": "Torno CNC 04",
                    "tipo": "Torno",
                    "setor": "Usinagem",
                    "status": "em operacao"
                }
                """;

        mockMvc.perform(post("/maquinas/cadastro")
                .contentType("application/json")
                .content(maquina))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.nome").value("Torno CNC 04"))
                .andExpect(jsonPath("$.setor").value("Usinagem"));
    }

    // Teste 3 - Buscar maquina por ID
    @Test
    void testarBuscarMaquinaPorId() throws Exception {

        mockMvc.perform(get("/maquinas/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Prensa Hidraulica 02"));
    }

    // Teste 4 - Buscar maquina inexistente
    @Test
    void testarMaquinaInexistente() throws Exception {

        mockMvc.perform(get("/maquinas/99"))
                .andExpect(status().isNotFound());
    }

    // Teste 5 - Atualizar o status de funcionamento
    @Test
    void testarAtualizarStatus() throws Exception {

        String status = """
                {
                    "status": "parada"
                }
                """;

        mockMvc.perform(put("/maquinas/1/status")
                .contentType("application/json")
                .content(status))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("parada"));
    }
}
