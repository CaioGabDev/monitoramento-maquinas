package com.senai.monitoramento.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
@TestPropertySource(properties = "app.versao=1.0.0")
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Teste 1 - Pagina inicial
    @Test
    void testarPaginaInicial() throws Exception {

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").exists())
                .andExpect(jsonPath("$.endpoints").isArray());
    }

    // Teste 2 - Status da aplicacao (RF05)
    @Test
    void testarStatusDaAplicacao() throws Exception {

        mockMvc.perform(get("/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("online"))
                .andExpect(jsonPath("$.mensagem").value("Aplicacao de monitoramento em execucao"));
    }

    // Teste 3 - Versao da aplicacao
    @Test
    void testarVersaoDaAplicacao() throws Exception {

        mockMvc.perform(get("/versao"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.aplicacao").value("monitoramento-maquinas"))
                .andExpect(jsonPath("$.versao").value("1.0.0"));
    }
}
