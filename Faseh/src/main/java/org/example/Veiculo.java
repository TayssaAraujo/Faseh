package br.com.faculdade.estacionamento.model;

import java.time.LocalDateTime;

public class Veiculo {
    private int id;
    private String placa;
    private String modelo;
    private String marca;
    private String tipo; // "pequeno", "grande", "moto"
    private LocalDateTime horaEntrada;
    private String vagaOcupada;
    private String usuarioRegistro;

    public Veiculo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(LocalDateTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public String getVagaOcupada() {
        return vagaOcupada;
    }

    public void setVagaOcupada(String vagaOcupada) {
        this.vagaOcupada = vagaOcupada;
    }

    public String getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(String usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    @Override
    public String toString() {
        return String.format(
                "| PLACA: %-7s | MODELO: %-15s | TIPO: %-8s | ENTRADA: %s | VAGA: %s |",
                placa, modelo, tipo, horaEntrada.toLocalTime().toString().substring(0, 8), vagaOcupada
        );
    }

}
