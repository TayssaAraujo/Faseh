package br.com.faculdade.estacionamento.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/estacionamento_faculdade?useTimezone=true&serverTimezone=UTC";
    private static final String USER = "root"; // Seu usuário do MySQL
    private static final String PASSWORD = "Assyat.123"; // Sua senha do MySQL

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Erro: Driver JDBC não encontrado.");
            throw new SQLException("Driver JDBC não encontrado.", e);
        }
    }

    /**
     * Fecha a conexão com o banco de dados.
     * @param conn Conexão a ser fechada.
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }
}