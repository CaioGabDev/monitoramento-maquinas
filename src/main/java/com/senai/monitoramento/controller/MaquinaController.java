package com.senai.monitoramento.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import com.senai.monitoramento.models.Maquina;

@RestController
@RequestMapping("/maquinas")
public class MaquinaController {

    // Lista que simula um banco de dados
    private List<Maquina> maquinas = new ArrayList<>();

    // Construtor - Maquinas iniciais
    public MaquinaController() {

        maquinas.add(new Maquina(1, "Maquina CNC 01", "CNC", "Usinagem", "em operacao"));
        maquinas.add(new Maquina(2, "Prensa Hidraulica 02", "Prensa", "Estamparia", "parada"));
        maquinas.add(new Maquina(3, "Robo de Solda 03", "Robo", "Soldagem", "em manutencao"));
    }

    // GET /maquinas - RF03 - Consultar maquinas cadastradas
    @GetMapping
    public ResponseEntity<List<Maquina>> listarMaquinas() {

        // HTTP 200 - OK
        return ResponseEntity.ok(maquinas);
    }

    // POST /maquinas/cadastro - RF01 - Cadastrar maquina
    @PostMapping("/cadastro")
    public ResponseEntity<Maquina> cadastrarMaquina(
            @RequestBody Maquina maquina) {

        // Se o id nao for informado, gera o proximo da sequencia
        if (maquina.getId() == 0) {
            maquina.setId(proximoId());
        }

        maquinas.add(maquina);

        // HTTP 201 - Created
        return ResponseEntity.status(201).body(maquina);
    }

    // GET /maquinas/{id} - Consultar uma maquina especifica
    @GetMapping("/{id}")
    public ResponseEntity<Maquina> buscarMaquina(
            @PathVariable int id) {

        for (Maquina maquina : maquinas) {

            if (maquina.getId() == id) {
                return ResponseEntity.ok(maquina);
            }
        }

        // HTTP 404 - Not Found
        return ResponseEntity.notFound().build();
    }

    // PUT /maquinas/{id}/status - Atualizar o status de funcionamento
    @PutMapping("/{id}/status")
    public ResponseEntity<Maquina> atualizarStatus(
            @PathVariable int id,
            @RequestBody Maquina dados) {

        for (Maquina maquina : maquinas) {

            if (maquina.getId() == id) {

                maquina.setStatus(dados.getStatus());
                return ResponseEntity.ok(maquina);
            }
        }

        // HTTP 404 - Not Found
        return ResponseEntity.notFound().build();
    }

    // Calcula o proximo id disponivel
    private int proximoId() {

        int maiorId = 0;

        for (Maquina maquina : maquinas) {

            if (maquina.getId() > maiorId) {
                maiorId = maquina.getId();
            }
        }

        return maiorId + 1;
    }
}
