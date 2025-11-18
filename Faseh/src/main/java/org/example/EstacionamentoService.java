package br.com.faculdade.estacionamento.service;

import br.com.faculdade.estacionamento.dao.VeiculoDAO;
import br.com.faculdade.estacionamento.model.Veiculo;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EstacionamentoService {
    private final VeiculoDAO veiculoDAO;
    private static final int CAPACIDADE_MAXIMA = 50;
    private static final double PRECO_PEQUENO = 16.00;
    private static final double PRECO_GRANDE = 25.00;
    private static final double PRECO_MOTO = 8.00;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EstacionamentoService() {
        this.veiculoDAO = new VeiculoDAO();
    }

    public String registrarEntrada(Veiculo veiculo) throws SQLException {
        if (veiculoDAO.buscarPorPlaca(veiculo.getPlaca()) != null) {
            return "{\"status\": \"erro\", \"mensagem\": \"Placa já registrada. Veículo ainda está no estacionamento.\"}";
        }
        if (veiculoDAO.contarVeiculos() >= CAPACIDADE_MAXIMA) {
            return "{\"status\": \"erro\", \"mensagem\": \"Estacionamento lotado. Capacidade máxima de " + CAPACIDADE_MAXIMA + " atingida.\"}";
        }
        veiculo.setPlaca(veiculo.getPlaca().toUpperCase());
        veiculo.setHoraEntrada(LocalDateTime.now());

        boolean sucesso = veiculoDAO.registrarEntrada(veiculo);

        if (sucesso) {
            return "{\"status\": \"sucesso\", \"mensagem\": \"Entrada registrada para a placa " + veiculo.getPlaca() + " na vaga " + veiculo.getVagaOcupada() + ".\"}";
        } else {
            return "{\"status\": \"erro\", \"mensagem\": \"Erro desconhecido ao registrar a entrada no banco.\"}";
        }
    }

    public String consultarStatus() throws SQLException {
        int totalCarros = veiculoDAO.contarVeiculos();
        int vagasLivres = CAPACIDADE_MAXIMA - totalCarros;
        List<Veiculo> veiculos = veiculoDAO.listarVeiculosEstacionados();

        StringBuilder sb = new StringBuilder();
        sb.append("--- STATUS DO ESTACIONAMENTO ---\n");
        sb.append("Capacidade Máxima: ").append(CAPACIDADE_MAXIMA).append(" vagas\n");
        sb.append("Total de Veículos Estacionados: ").append(totalCarros).append("\n");
        sb.append("Vagas Livres: ").append(vagasLivres).append("\n");
        sb.append("------------------------------\n");
        sb.append("VAGAS OCUPADAS:\n");

        if (veiculos.isEmpty()) {
            sb.append("Nenhum veículo estacionado no momento.\n");
        } else {
            for (Veiculo v : veiculos) {
                sb.append(v.toString()).append("\n");
            }
        }

        return sb.toString();
    }

    public String registrarSaidaECalcularPagamento(String placa, String usuarioSaida) throws SQLException {
        Veiculo veiculo = veiculoDAO.buscarPorPlaca(placa);

        if (veiculo == null) {
            return "{\"status\": \"erro\", \"mensagem\": \"Placa não encontrada. Veículo não registrado no estacionamento.\"}";
        }

        LocalDateTime horaSaida = LocalDateTime.now();
        LocalDateTime horaEntrada = veiculo.getHoraEntrada();

        Duration duracao = Duration.between(horaEntrada, horaSaida);

        long minutos = duracao.toMinutes();
        long tempoPermanenciaHoras = (minutos + 59) / 60;

        double valorHora;
        switch (veiculo.getTipo().toLowerCase()) {
            case "pequeno":
                valorHora = PRECO_PEQUENO;
                break;
            case "grande":
                valorHora = PRECO_GRANDE;
                break;
            case "moto":
                valorHora = PRECO_MOTO;
                break;
            default:
                valorHora = 0.0;
        }

        double valorTotal = tempoPermanenciaHoras * valorHora;

        boolean removido = veiculoDAO.registrarSaida(placa);

        if (!removido) {
            return "{\"status\": \"erro\", \"mensagem\": \"Falha ao remover o registro do veículo. Cobrança não finalizada.\"}";
        }

        return String.format(
                "{\n" +
                        "  \"placa\": \"%s\",\n" +
                        "  \"hora_entrada\": \"%s\",\n" +
                        "  \"hora_saida\": \"%s\",\n" +
                        "  \"tempo_permanencia_minutos\": %d,\n" +
                        "  \"tempo_permanencia_horas\": %d,\n" +
                        "  \"valor_hora\": %.2f,\n" +
                        "  \"valor_total\": %.2f,\n" +
                        "  \"usuario_registro_saida\": \"%s\"\n" +
                        "}",
                veiculo.getPlaca(),
                horaEntrada.format(FORMATTER),
                horaSaida.format(FORMATTER),
                minutos,
                tempoPermanenciaHoras,
                valorHora,
                valorTotal,
                usuarioSaida
        );
    }
}