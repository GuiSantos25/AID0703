# UFCD0791 - TP3 - Código Robusto com Tratamento de Exceções

**Nome:** [Guilherme Santos]
**Turma:** AID07

## 1. Versão do JDK

Desenvolvido e testado com **JDK 17** (compatível com qualquer distribuição JDK 11+,
uma vez que o código apenas usa `String.isBlank()` como funcionalidade mínima de Java 11).
Verificar a versão instalada com:

```
java -version
javac -version
```

## 2. Instruções de Compilação

A partir da raiz do projeto (`processador/`):

```
javac -d bin src/excecoes/*.java src/*.java
```

Isto gera os `.class` dentro da pasta `bin/`, preservando o pacote `excecoes`.

## 3. Instruções de Execução

A partir da raiz do projeto, após compilar:

```
java -cp bin Principal caminho/para/movimentos.txt caminho/para/relatorio.txt
```

- `args[0]` — caminho do ficheiro de entrada com os movimentos a processar.
- `args[1]` — caminho do ficheiro de relatório a gerar (é criado/substituído).

Exemplo:

```
java -cp bin Principal dados/movimentos.txt dados/relatorio.txt
```

## 4. Formato de Entrada e Sintaxe de Suporte a Erros

Cada linha do ficheiro de entrada deve seguir o formato:

```
CLIENTE;TIPO_OPERACAO;VALOR;DESCRICAO
```

Exemplo de ficheiro válido com casos de erro incluídos:

```
Joao Silva;TRANSFERENCIA;150.50;Pagamento renda
Maria Costa;DEPOSITO;1000;Deposito a prazo
Pedro Alves;LEVANTAMENTO;abc;Levantamento ATM
Ana Ferreira;TRANSFERENCIA;-200;Transferencia invalida
Linha sem delimitadores suficientes
;TRANSFERENCIA;300;Cliente em falta
```

Regras de validação:

- **Erro de formato** (`FormatoLinhaInvalidaException`, checked): linha vazia, número de
  campos diferente de 4, ou campos `CLIENTE`/`TIPO_OPERACAO` em branco.
- **Erro de valor** (`ValorMovimentoInvalidoException`, unchecked): campo `VALOR` não
  numérico (ex.: "abc"), nulo ou negativo (ex.: -200).
- Qualquer linha rejeitada é registada na secção de anomalias do relatório com o número
  da linha, o tipo de erro e a mensagem explicativa; o processamento continua sempre
  para a linha seguinte.
- Ficheiro de entrada inexistente ou inacessível: a aplicação captura a `IOException`
  em `Principal`, informa o utilizador e termina de forma controlada (sem crash).
- Ficheiro de entrada vazio: é gerado um relatório com zero movimentos válidos e uma
  anomalia a assinalar a ausência de registos.

## 5. Justificação Técnica: checked vs unchecked e padrão de propagação

Optou-se por `FormatoLinhaInvalidaException` como *checked exception* (herdeira de
`Exception`) porque um erro estrutural de sintaxe é uma condição de negócio antecipável
e recuperável do próprio ficheiro de integração, pelo que o compilador deve obrigar
todos os pontos de chamada na cadeia de orquestração (`LinhaMovimento` → `ProcessadorMovimentos`)
a decidir explicitamente como reagir, tornando o contrato de erro visível na assinatura
dos métodos. Já `ValorMovimentoInvalidoException` foi implementada como *unchecked*
(herdeira de `RuntimeException`) por representar uma falha de conversão de dados,
semanticamente próxima de `NumberFormatException`, que ocorre num ponto muito localizado
(a conversão do campo monetário) e cuja propagação não deve poluir com `throws` todos os
métodos intermédios que não têm responsabilidade de negócio sobre essa validação. O
padrão de propagação adotado — `throw` nos métodos de conversão de `LinhaMovimento`,
`throws` explícito apenas para a exceção checked nos métodos de orquestração de
`ProcessadorMovimentos`, e captura centralizada de ambas em `processarLinhaIsolada` —
garante que a responsabilidade de decidir "o que fazer" com o erro fica concentrada na
camada de gestão do lote, mantendo `LinhaMovimento` focado unicamente na validação
(Single Responsibility Principle) e assegurando que uma linha inválida nunca aborta o
processamento das restantes.

## 6. Nota sobre try-with-resources

Não é utilizado try-with-resources em nenhum ponto do fluxo principal de I/O
(leitura do ficheiro de movimentos em `ProcessadorMovimentos` e escrita do relatório em
`RelatorioService`). Em ambos os casos os recursos (`BufferedReader` e `PrintWriter`) são
geridos manualmente com blocos `try-catch-finally`, incluindo verificação de não-nulidade
e captura de `IOException` no fecho, conforme exigido pelas restrições técnicas do
enunciado.
