package br.com.faculdade.estacionamento;

import br.com.faculdade.estacionamento.model.Veiculo;
import br.com.faculdade.estacionamento.service.EstacionamentoService;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final EstacionamentoService service = new EstacionamentoService();

    public static void main(String[] args) {
        System.out.println("Sistema de Controle de Estacionamento - Faseh");

        // Teste inicial de conexão
        try {
            // Tentar qualquer operação de DAO para verificar a conexão
            service.consultarStatus();
            System.out.println("Conexão com o MySQL bem-sucedida.");
        } catch (SQLException e) {
            System.err.println("\nERRO FATAL: Falha ao conectar ao banco de dados.");
            System.err.println("Por favor, verifique o DbConnection.java e as credenciais do MySQL.");
            System.err.println("Detalhes do erro: " + e.getMessage());
            return; // Encerra o programa
        }

        int opcao = 0;
        while (opcao != 4) {
            exibirMenu();
            try {
                opcao = scanner.nextInt();
                scanner.nextLine(); // Consumir a nova linha

                switch (opcao) {
                    case 1:
                        registrarEntradaVeiculo();
                        break;
                    case 2:
                        consultarStatusVagas();
                        break;
                    case 3:
                        registrarSaidaECalcularPagamento();
                        break;
                    case 4:
                        System.out.println("Encerrando o sistema. Até mais!");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.err.println("Entrada inválida. Por favor, digite um número.");
                scanner.nextLine(); // Limpa o buffer
                opcao = 0;
            }
        }
    }

    private static void exibirMenu() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Registrar Entrada de Veículo");
        System.out.println("2. Consultar Status das Vagas");
        System.out.println("3. Registrar Saída e Calcular Pagamento");
        System.out.println("4. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void registrarEntradaVeiculo() {
        System.out.println("\n--- REGISTRAR ENTRADA ---");
        Veiculo veiculo = new Veiculo();

        System.out.print("Placa do Veículo (ex: ABC1234): ");
        veiculo.setPlaca(scanner.nextLine().trim().toUpperCase());

        System.out.print("Modelo: ");
        veiculo.setModelo(scanner.nextLine().trim());

        System.out.print("Marca: ");
        veiculo.setMarca(scanner.nextLine().trim());

        String tipo = "";
        while (true) {
            System.out.print("Tipo (pequeno, grande, moto): ");
            tipo = scanner.nextLine().trim().toLowerCase();
            if (tipo.equals("pequeno") || tipo.equals("grande") || tipo.equals("moto")) {
                veiculo.setTipo(tipo);
                break;
            }
            System.out.println("Tipo inválido. Por favor, insira 'pequeno', 'grande' ou 'moto'.");
        }

        System.out.print("Vaga Ocupada (ex: A01, opcional): ");
        veiculo.setVagaOcupada(scanner.nextLine().trim().toUpperCase());

        System.out.print("Nome do Usuário/Funcionário Registrador: ");
        veiculo.setUsuarioRegistro(scanner.nextLine().trim());

        try {
            String resultado = service.registrarEntrada(veiculo);
            // Simula a exibição formatada da resposta da "API"
            System.out.println("\n--- RESPOSTA DA OPERAÇÃO ---\n" + resultado + "\n----------------------------");
        } catch (SQLException e) {
            System.err.println("Erro ao interagir com o banco de dados: " + e.getMessage());
        }
    }

    private static void consultarStatusVagas() {
        try {
            String status = service.consultarStatus();
            System.out.println("\n" + status);
        } catch (SQLException e) {
            System.err.println("Erro ao consultar o status das vagas: " + e.getMessage());
        }
    }

    private static void registrarSaidaECalcularPagamento() {
        System.out.println("\n--- REGISTRAR SAÍDA E COBRANÇA ---");

        System.out.print("Placa do Veículo para saída: ");
        String placa = scanner.nextLine().trim().toUpperCase();

        System.out.print("Nome do Usuário/Funcionário Registrador da Saída: ");
        String usuarioSaida = scanner.nextLine().trim();

        try {
            String jsonCobranca = service.registrarSaidaECalcularPagamento(placa, usuarioSaida);

            // Simula a exibição formatada da resposta JSON de cobrança
            if (jsonCobranca.contains("\"status\": \"erro\"")) {
                System.err.println("\n--- ERRO NA SAÍDA ---\n" + jsonCobranca + "\n-----------------------");
            } else {
                System.out.println("\n--- COBRANÇA FINALIZADA (JSON) ---");
                System.out.println(jsonCobranca);
                System.out.println("------------------------------------");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao interagir com o banco de dados durante a saída: " + e.getMessage());
        }
    }
}