package excecoes;

/**
 * Exceção checked lançada quando um registo do ficheiro de movimentos
 * viola as regras estruturais de sintaxe (campos em falta, delimitadores
 * ausentes, linha vazia, etc.).
 *
 * É uma checked exception (herda de {@link java.lang.Exception}) porque
 * representa uma condição de negócio previsível e recuperável: o chamador
 * é obrigado, em tempo de compilação, a decidir como reagir a um registo
 * estruturalmente inválido.
 */
public class FormatoLinhaInvalidaException extends Exception {

    /**
     * Constrói a exceção com uma mensagem explicativa do erro de formato.
     *
     * @param message descrição do motivo da invalidade estrutural
     */
    public FormatoLinhaInvalidaException(String message) {
        super(message);
    }

    /**
     * Constrói a exceção encadeando a causa original que despoletou o erro.
     *
     * @param message descrição do motivo da invalidade estrutural
     * @param cause   exceção original que originou esta falha
     */
    public FormatoLinhaInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }
}
