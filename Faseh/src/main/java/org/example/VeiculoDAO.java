package br.com.faculdade.estacionamento.dao;

import br.com.faculdade.estacionamento.model.Veiculo;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VeiculoDAO {

    public boolean registrarEntrada(Veiculo veiculo) throws SQLException {
        String sql = "INSERT INTO veiculos (placa, modelo, marca, tipo, vaga_ocupada, usuario_registro) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, veiculo.getPlaca().toUpperCase());
            stmt.setString(2, veiculo.getModelo());
            stmt.setString(3, veiculo.getMarca());
            stmt.setString(4, veiculo.getTipo().toLowerCase());
            stmt.setString(5, veiculo.getVagaOcupada());
            stmt.setString(6, veiculo.getUsuarioRegistro());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } finally {
            if (stmt != null) stmt.close();
            DbConnection.closeConnection(conn);
        }
    }

    public Veiculo buscarPorPlaca(String placa) throws SQLException {
        String sql = "SELECT id, placa, modelo, marca, tipo, hora_entrada, vaga_ocupada, usuario_registro FROM veiculos WHERE placa = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, placa.toUpperCase());
            rs = stmt.executeQuery();

            if (rs.next()) {
                Veiculo veiculo = new Veiculo();
                veiculo.setId(rs.getInt("id"));
                veiculo.setPlaca(rs.getString("placa"));
                veiculo.setModelo(rs.getString("modelo"));
                veiculo.setMarca(rs.getString("marca"));
                veiculo.setTipo(rs.getString("tipo"));
                Timestamp ts = rs.getTimestamp("hora_entrada");
                veiculo.setHoraEntrada(ts.toLocalDateTime());

                veiculo.setVagaOcupada(rs.getString("vaga_ocupada"));
                veiculo.setUsuarioRegistro(rs.getString("usuario_registro"));
                return veiculo;
            }
            return null; 

        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DbConnection.closeConnection(conn);
        }
    }

    public int contarVeiculos() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM veiculos";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DbConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;

        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DbConnection.closeConnection(conn);
        }
    }

    public List<Veiculo> listarVeiculosEstacionados() throws SQLException {
        String sql = "SELECT placa, modelo, tipo, hora_entrada, vaga_ocupada FROM veiculos ORDER BY hora_entrada ASC";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Veiculo> veiculos = new ArrayList<>();

        try {
            conn = DbConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Veiculo veiculo = new Veiculo();
                veiculo.setPlaca(rs.getString("placa"));
                veiculo.setModelo(rs.getString("modelo"));
                veiculo.setTipo(rs.getString("tipo"));

                Timestamp ts = rs.getTimestamp("hora_entrada");
                veiculo.setHoraEntrada(ts.toLocalDateTime());

                veiculo.setVagaOcupada(rs.getString("vaga_ocupada"));
                veiculos.add(veiculo);
            }
            return veiculos;

        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DbConnection.closeConnection(conn);
        }
    }

    public boolean registrarSaida(String placa) throws SQLException {
        String sql = "DELETE FROM veiculos WHERE placa = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, placa.toUpperCase());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } finally {
            if (stmt != null) stmt.close();
            DbConnection.closeConnection(conn);
        }
    }

}
