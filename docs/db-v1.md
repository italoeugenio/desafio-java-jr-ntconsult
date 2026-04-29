# 📘 Banco de Dados de Desafio de Assinantes

Este banco de dados armazena informações sobre os livros cadastrados no sistema

---

## Tabela: `TB_LIVROS`

Armazena as informações dos livros.

| Coluna             | Tipo        | Descrição                                         | Notas              |
| :----------------- | :---------- | :------------------------------------------------ | :----------------- |
| `id`               | `bigint`    | Identificador único de cada livro                 | **Chave Primária** |
| `titulo`           | `string`    | Título completo da obra                           |                    |
| `autor`            | `string`    | Nome do autor do livro                            |                    |
| `ano_publicacao`   | `integer`   | Ano em que o livro foi publicado                  |                    |
| `criado_em`        | `timestamp` | Data e hora em que o registro foi criado          | Valor padrão: Now  |
| `atualizado_em`    | `timestamp` | Data e hora da última atualização do registro     | Valor padrão: Now  |

---

### Observações Técnicas (Entidade Java)

* **Estratégia de ID:** A geração do ID está configurada como `IDENTITY`, o que delega ao banco de dados a responsabilidade do auto-incremento.
* **Mapeamento JPA:** A entidade utiliza o Lombok para reduzir o código boilerplate (`Getters`, `Setters`, `NoArgsConstructor`, etc).
* **Auditoria Simples:** Os campos `criadoEm` e `atualizadoEm` estão inicializados com o horário atual da aplicação no momento da instância.