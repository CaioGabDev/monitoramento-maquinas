package com.senai.monitoramento.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.senai.monitoramento.models.Producao;

@RestController
@RequestMapping("/producao")
public class ProducaoController {

    // Lista que simula um banco de dados
    private List<Producao> registros = new ArrayList<>();

    // Construtor - Registros iniciais
    public ProducaoController() {

        registros.add(new Producao(1, 1, 12.5, "08:30", 150));
        registros.add(new Producao(2, 1, 10.2, "13:00", 120));
        registros.add(new Producao(3, 2, 8.75, "09:15", 80));
    }

    // GET /producao - RF04 - Consultar dados de producao e consumo
    @GetMapping
    public ResponseEntity<List<Producao>> listarProducao() {

        // HTTP 200 - OK
        return ResponseEntity.ok(registros);
    }

    // POST /producao - RF02 - Registrar dados de producao
    @PostMapping
    public ResponseEntity<Producao> registrarProducao(
            @RequestBody Producao producao) {

        // Validacao basica dos dados recebidos
        if (producao.getMaquinaId() <= 0
                || producao.getConsumoKw() < 0
                || producao.getPecasProduzidas() < 0) {

            // HTTP 400 - Bad Request
            return ResponseEntity.badRequest().build();
        }

        producao.setId(proximoId());
        registros.add(producao);

        // HTTP 201 - Created
        return ResponseEntity.status(201).body(producao);
    }

    // GET /producao/maquina/{id} - Historico basico de producao e consumo
    @GetMapping("/maquina/{id}")
    public ResponseEntity<Map<String, Object>> historicoDaMaquina(
            @PathVariable int id) {

        List<Producao> historico = new ArrayList<>();

        double totalConsumo = 0;
        int totalPecas = 0;

        for (Producao producao : registros) {

            if (producao.getMaquinaId() == id) {

                historico.add(producao);
                totalConsumo = totalConsumo + producao.getConsumoKw();
                totalPecas = totalPecas + producao.getPecasProduzidas();
            }
        }

        if (historico.isEmpty()) {

            // HTTP 404 - Not Found
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> resposta = new LinkedHashMap<>();

        resposta.put("maquinaId", id);
        resposta.put("totalConsumoKw", totalConsumo);
        resposta.put("totalPecasProduzidas", totalPecas);
        resposta.put("historico", historico);

        return ResponseEntity.ok(resposta);
    }

    // Calcula o proximo id disponivel
    private int proximoId() {

        int maiorId = 0;

        for (Producao producao : registros) {

            if (producao.getId() > maiorId) {
                maiorId = producao.getId();
            }
        }

        return maiorId + 1;
    }
}
