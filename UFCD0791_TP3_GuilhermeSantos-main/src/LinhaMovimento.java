import excecoes.FormatoLinhaInvalidaException;
import excecoes.ValorMovimentoInvalidoException;

/**
 * Representa um registo de movimento bancário já validado.
 * Única responsabilidade: converter e validar uma linha de texto no
 * formato CLIENTE;TIPO_OPERACAO;VALOR;DESCRICAO num objeto de domínio.
 */
public class LinhaMovimento {

    private static final String DELIMITADOR = ";";
    private static final int NUMERO_CAMPOS_ESPERADO = 4;
    private static final int INDICE_CLIENTE = 0;
    private static final int INDICE_TIPO_OPERACAO = 1;
    private static final int INDICE_VALOR = 2;
    private static final int INDICE_DESCRICAO = 3;

    private final String cliente;
    private final String tipoOperacao;
    private final double valor;
    private final String descricao;

    private LinhaMovimento(String cliente, String tipoOperacao, double valor, String descricao) {
        this.cliente = cliente;
        this.tipoOperacao = tipoOperacao;
        this.valor = valor;
        this.descricao = descricao;
    }

    /**
     * Analisa e valida uma linha de texto do ficheiro de movimentos.
     *
     * @param linha linha lida do ficheiro de entrada
     * @return instância validada de {@code LinhaMovimento}
     * @throws FormatoLinhaInvalidaException se a estrutura sintática da linha for inválida
     * @throws ValorMovimentoInvalidoException se o valor monetário for inconvertível, nulo ou negativo
     */
    public static LinhaMovimento parse(String linha) throws FormatoLinhaInvalidaException {
        String[] campos = separarCampos(linha);
        validarCamposObrigatorios(campos);

        String cliente = campos[INDICE_CLIENTE].trim();
        String tipoOperacao = campos[INDICE_TIPO_OPERACAO].trim();
        double valor = converterValor(campos[INDICE_VALOR].trim());
        String descricao = campos[INDICE_DESCRICAO].trim();

        return new LinhaMovimento(cliente, tipoOperacao, valor, descricao);
    }

    private static String[] separarCampos(String linha) throws FormatoLinhaInvalidaException {
        if (linha == null || linha.isBlank()) {
            throw new FormatoLinhaInvalidaException("Linha vazia ou nula.");
        }
        String[] campos = linha.split(DELIMITADOR, -1);
        if (campos.length != NUMERO_CAMPOS_ESPERADO) {
            throw new FormatoLinhaInvalidaException(
                "Numero de campos invalido: esperado " + NUMERO_CAMPOS_ESPERADO
                    + ", encontrado " + campos.length + ".");
        }
        return campos;
    }

    private static void validarCamposObrigatorios(String[] campos) throws FormatoLinhaInvalidaException {
        if (campos[INDICE_CLIENTE].isBlank() || campos[INDICE_TIPO_OPERACAO].isBlank()) {
            throw new FormatoLinhaInvalidaException("Campos CLIENTE ou TIPO_OPERACAO em falta.");
        }
    }

    /**
     * Converte e valida o campo monetário. Lança {@link ValorMovimentoInvalidoException}
     * (unchecked) perante texto não numérico ou montantes nulos/negativos.
     */
    private static double converterValor(String valorTexto) {
        double valor;
        try {
            valor = Double.parseDouble(valorTexto);
        } catch (NumberFormatException causaOriginal) {
            throw new ValorMovimentoInvalidoException(
                "Valor monetario nao numerico: '" + valorTexto + "'.", causaOriginal);
        }
        if (valor <= 0) {
            throw new ValorMovimentoInvalidoException("Valor monetario nulo ou negativo: " + valor + ".");
        }
        return valor;
    }

    public String getCliente() {
        return cliente;
    }

    public String getTipoOperacao() {
        return tipoOperacao;
    }

    public double getValor() {
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return cliente + ";" + tipoOperacao + ";" + valor + ";" + descricao;
    }
}
