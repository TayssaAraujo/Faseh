
# Sistema de Estacionamento (Java/MySQL)

 Sistema simples de controle de estacionamento feito em Java (JDBC) com dados armazenados no banco MySQL, com operações são feitas via terminal.

1.  Entrada: Registra o carro e a hora atual.
2.  Consulta: Mostra quantos carros estão e quantas vagas faltam.
3.  Saída e Cobrança: Calcula o valor a pagar.

### 1\. Banco de Dados (MySQL)

```sql
-- Crie o banco
CREATE DATABASE estacionamento_faculdade;
USE estacionamento_faculdade;

-- Crie a tabela
CREATE TABLE veiculos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    placa VARCHAR(10) UNIQUE NOT NULL,
    tipo VARCHAR(10), -- pequeno, grande, moto
    hora_entrada TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    -- ... outros campos de dados (modelo, marca, etc.)
);
```

### 2\. Configuração no Java

```java
// dao/DbConnection.java
private static final String USER = "seu_usuario";
private static final String PASSWORD = "sua_senha";
```
