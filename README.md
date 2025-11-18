
# Sistema de Estacionamento Faseh

 Sistema simples de controle de estacionamento feito em Java com dados armazenados no banco Mysql, com operações são feitas via terminal.

1.  Entrada: Registra o carro e a hora atual.
2.  Consulta: Mostra quantos carros estão e quantas vagas faltam.
3.  Saída e Cobrança: Calcula o valor a pagar.

### 1\. Banco de Dados

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
### 3\. testada conexão pelo Dbeaver
<img width="1488" height="624" alt="image" src="https://github.com/user-attachments/assets/ae529213-d97f-4d16-b478-7fe28665fc90" />
