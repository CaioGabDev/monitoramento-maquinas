package com.senai.monitoramento.models;

public class Producao {

    // Atributos dos dados de producao (RF02)

    private int id;
    private int maquinaId;
    private double consumoKw;
    private String horarioAcionamento;
    private int pecasProduzidas;

    // Construtor vazio - necessario para o Spring converter o JSON recebido
    public Producao() {
    }

    // Construtor

    public Producao(int id, int maquinaId, double consumoKw,
            String horarioAcionamento, int pecasProduzidas) {
        this.id = id;
        this.maquinaId = maquinaId;
        this.consumoKw = consumoKw;
        this.horarioAcionamento = horarioAcionamento;
        this.pecasProduzidas = pecasProduzidas;
    }

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaquinaId() {
        return maquinaId;
    }

    public void setMaquinaId(int maquinaId) {
        this.maquinaId = maquinaId;
    }

    public double getConsumoKw() {
        return consumoKw;
    }

    public void setConsumoKw(double consumoKw) {
        this.consumoKw = consumoKw;
    }

    public String getHorarioAcionamento() {
        return horarioAcionamento;
    }

    public void setHorarioAcionamento(String horarioAcionamento) {
        this.horarioAcionamento = horarioAcionamento;
    }

    public int getPecasProduzidas() {
        return pecasProduzidas;
    }

    public void setPecasProduzidas(int pecasProduzidas) {
        this.pecasProduzidas = pecasProduzidas;
    }
}
