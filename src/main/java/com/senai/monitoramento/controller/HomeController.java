package com.senai.monitoramento.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HomeController {

    // Versao lida do application.properties
    @Value("${app.versao}")
    private String versao;

    // GET / - Pagina inicial
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> paginaInicial() {

        Map<String, Object> resposta = new LinkedHashMap<>();

        resposta.put("mensagem",
                "Bem-vindo ao sistema de monitoramento de maquinas da CloudTech Solutions");
        resposta.put("endpoints", List.of(
                "/",
                "/status",
                "/versao",
                "/maquinas",
                "/maquinas/cadastro",
                "/maquinas/{id}",
                "/producao",
                "/producao/maquina/{id}"));

        // HTTP 200 - OK
        return ResponseEntity.ok(resposta);
    }

    // GET /status - RF05 - Verificar status da aplicacao
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> status() {

        Map<String, Object> resposta = new LinkedHashMap<>();

        resposta.put("status", "online");
        resposta.put("mensagem", "Aplicacao de monitoramento em execucao");

        return ResponseEntity.ok(resposta);
    }

    // GET /versao - Versao da aplicacao
    @GetMapping("/versao")
    public ResponseEntity<Map<String, Object>> versao() {

        Map<String, Object> resposta = new LinkedHashMap<>();

        resposta.put("aplicacao", "monitoramento-maquinas");
        resposta.put("versao", versao);

        return ResponseEntity.ok(resposta);
    }
}
