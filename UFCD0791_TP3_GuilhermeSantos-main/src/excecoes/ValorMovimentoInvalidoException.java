package excecoes;

/**
 * Exceção unchecked lançada quando o campo monetário de um movimento
 * é inconvertível, nulo ou negativo.
 *
 * É uma unchecked exception (herda de {@link java.lang.RuntimeException})
 * porque representa uma falha de validação de dados detetada em tempo
 * de execução durante a conversão numérica, à semelhança de
 * {@link NumberFormatException}, não obrigando à declaração explícita
 * de throws em toda a cadeia de chamadas de conversão.
 */
public class ValorMovimentoInvalidoException extends RuntimeException {

    /**
     * Constrói a exceção com uma mensagem explicativa do erro de valor.
     *
     * @param message descrição do motivo da invalidade do valor monetário
     */
    public ValorMovimentoInvalidoException(String message) {
        super(message);
    }

    /**
     * Constrói a exceção encadeando a causa original que despoletou o erro.
     *
     * @param message descrição do motivo da invalidade do valor monetário
     * @param cause   exceção original que originou esta falha (ex.: NumberFormatException)
     */
    public ValorMovimentoInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }
}
