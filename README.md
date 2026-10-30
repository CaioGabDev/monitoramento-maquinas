# Monitoramento de Máquinas — CloudTech Solutions

Exercício 1 — Aula 11 de Desenvolvimento Backend (Prof. Me Daniel Vieira).

Aplicação Java + Spring Boot para cadastro, consulta e acompanhamento de máquinas
industriais: tipo de máquina, consumo de energia em kW, horário de acionamento,
quantidade de peças produzidas, status de funcionamento e histórico básico de
produção e consumo.

## Tecnologias

- Java 21
- Spring Boot 4.1.1 (`spring-boot-starter-webmvc`)
- Maven
- JUnit 5 + MockMvc (`spring-boot-starter-webmvc-test`)
- GitHub Actions (pipeline CI)

## Como executar

```bash
mvn spring-boot:run
```

A aplicação sobe em `http://localhost:8081`.

Para gerar o JAR e rodar os testes:

```bash
mvn clean package
java -jar target/monitoramento-maquinas-0.0.1-SNAPSHOT.jar
```

## Endpoints

| Método | Endpoint                  | Descrição                                    | Requisito |
|--------|---------------------------|----------------------------------------------|-----------|
| GET    | `/`                       | Página inicial com a lista de endpoints      | —         |
| GET    | `/status`                 | Status da aplicação                          | RF05      |
| GET    | `/versao`                 | Versão da aplicação                          | —         |
| GET    | `/maquinas`               | Listagem de máquinas cadastradas             | RF03      |
| POST   | `/maquinas/cadastro`      | Cadastro de máquina                          | RF01      |
| GET    | `/maquinas/{id}`          | Consulta de uma máquina específica           | RF03      |
| PUT    | `/maquinas/{id}/status`   | Atualiza o status de funcionamento           | —         |
| GET    | `/producao`               | Consulta dos dados de produção e consumo     | RF04      |
| POST   | `/producao`               | Registro de dados de produção                | RF02      |
| GET    | `/producao/maquina/{id}`  | Histórico de produção e consumo da máquina   | RF04      |

### RF01 — Cadastrar máquina

`POST /maquinas/cadastro`

```json
{
  "nome": "Máquina CNC 01",
  "tipo": "CNC",
  "setor": "Usinagem",
  "status": "em operação"
}
```

Resposta `201 Created`:

```json
{
  "id": 4,
  "nome": "Máquina CNC 01",
  "tipo": "CNC",
  "setor": "Usinagem",
  "status": "em operação"
}
```

O `id` é opcional no envio — quando omitido, a aplicação gera o próximo da sequência.

### RF02 — Registrar dados de produção

`POST /producao`

```json
{
  "maquinaId": 1,
  "consumoKw": 12.5,
  "horarioAcionamento": "08:30",
  "pecasProduzidas": 150
}
```

Retorna `201 Created`, ou `400 Bad Request` quando `maquinaId` é inválido ou os
valores de consumo/peças são negativos.

### RF05 — Status da aplicação

`GET /status`

```json
{
  "status": "online",
  "mensagem": "Aplicacao de monitoramento em execucao"
}
```

### Histórico de produção e consumo

`GET /producao/maquina/1`

```json
{
  "maquinaId": 1,
  "totalConsumoKw": 22.7,
  "totalPecasProduzidas": 270,
  "historico": [
    { "id": 1, "maquinaId": 1, "consumoKw": 12.5, "horarioAcionamento": "08:30", "pecasProduzidas": 150 },
    { "id": 2, "maquinaId": 1, "consumoKw": 10.2, "horarioAcionamento": "13:00", "pecasProduzidas": 120 }
  ]
}
```

## Testes

Testes automatizados (13 testes, MockMvc):

```bash
mvn test
```

Testes manuais com **Thunder Client** (VS Code): importe a coleção
[`tests/thunder-collection_monitoramento-maquinas.json`](tests/thunder-collection_monitoramento-maquinas.json)
em *Thunder Client → Collections → Import*. A coleção cobre todos os endpoints e
já traz as validações de status HTTP esperadas.

## Estrutura do projeto

```
monitoramento-maquinas/
├── src/
│   ├── main/java/com/senai/monitoramento/
│   │   ├── MonitoramentoApplication.java
│   │   ├── controller/   (HomeController, MaquinaController, ProducaoController)
│   │   └── models/       (Maquina, Producao)
│   └── test/java/com/senai/monitoramento/controller/
├── tests/                (coleção do Thunder Client)
├── README.md
├── .gitignore
└── .github/
    └── workflows/
        └── pipeline.yml
```

## Pipeline CI/CD

O workflow [`.github/workflows/pipeline.yml`](.github/workflows/pipeline.yml)
roda a cada push e pull request em `main`/`master`: configura o Java 21, executa
`mvn clean package` (compilação + testes) e publica o JAR como artefato da execução.

## Observações

Os dados ficam em memória (listas dentro dos controllers), como previsto para o
escopo da aula — ao reiniciar a aplicação, o acervo volta aos registros iniciais.
