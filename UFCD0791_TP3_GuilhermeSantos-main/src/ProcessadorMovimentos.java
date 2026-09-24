import excecoes.FormatoLinhaInvalidaException;
import excecoes.ValorMovimentoInvalidoException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Orquestra a leitura do ficheiro de movimentos e coordena o tratamento
 * resiliente de cada registo, garantindo que uma linha inválida nunca
 * interrompe o processamento do lote nem a libertação do recurso de leitura.
 */
public class ProcessadorMovimentos {

    private int totalLinhasLidas = 0;
    private int totalLinhasValidas = 0;
    private double somaValoresValidos = 0.0;
    private final List<String> anomalias = new ArrayList<>();
    private final List<LinhaMovimento> movimentosValidos = new ArrayList<>();

    /**
     * Processa o ficheiro de movimentos linha a linha. O canal de leitura é
     * sempre fechado no bloco finally, com verificação de não-nulidade e
     * captura interna de eventuais falhas de fecho.
     *
     * @param caminhoEntrada caminho do ficheiro de entrada
     * @throws IOException se o ficheiro não existir ou não puder ser aberto para leitura
     */
    public void processarFicheiro(String caminhoEntrada) throws IOException {
        BufferedReader leitor = null;
        try {
            leitor = new BufferedReader(new FileReader(caminhoEntrada));
            String linha;
            int numeroLinha = 0;
            while ((linha = leitor.readLine()) != null) {
                numeroLinha++;
                totalLinhasLidas++;
                processarLinhaIsolada(linha, numeroLinha);
            }
        } finally {
            fecharComSeguranca(leitor);
        }

        if (totalLinhasLidas == 0) {
            anomalias.add("Ficheiro de entrada vazio: nenhum registo encontrado.");
        }
    }

    private void fecharComSeguranca(BufferedReader leitor) {
        if (leitor != null) {
            try {
                leitor.close();
            } catch (IOException erroFecho) {
                anomalias.add("Aviso: falha ao fechar o ficheiro de entrada - " + erroFecho.getMessage());
            }
        }
    }

    /**
     * Delimita a fronteira de isolamento de uma única linha: intercepta tanto
     * a exceção checked de formato como a unchecked de valor, registando a
     * anomalia sem propagar a falha para o ciclo de leitura.
     */
    private void processarLinhaIsolada(String linha, int numeroLinha) {
        try {
            LinhaMovimento movimento = processarLinha(linha);
            registrarMovimentoValido(movimento);
        } catch (FormatoLinhaInvalidaException erroFormato) {
            registrarAnomalia(numeroLinha, "Formato invalido", erroFormato.getMessage());
        } catch (ValorMovimentoInvalidoException erroValor) {
            registrarAnomalia(numeroLinha, "Valor invalido", erroValor.getMessage());
        }
    }

    /**
     * Delega o parsing à camada de domínio, propagando o contrato checked
     * através da cláusula throws para o chamador.
     *
     * @throws FormatoLinhaInvalidaException se a linha violar a sintaxe esperada
     */
    private LinhaMovimento processarLinha(String linha) throws FormatoLinhaInvalidaException {
        return LinhaMovimento.parse(linha);
    }

    private void registrarMovimentoValido(LinhaMovimento movimento) {
        totalLinhasValidas++;
        somaValoresValidos += movimento.getValor();
        movimentosValidos.add(movimento);
    }

    private void registrarAnomalia(int numeroLinha, String tipoErro, String mensagem) {
        anomalias.add("Linha " + numeroLinha + " [" + tipoErro + "]: " + mensagem);
    }

    public int getTotalLinhasLidas() {
        return totalLinhasLidas;
    }

    public int getTotalLinhasValidas() {
        return totalLinhasValidas;
    }

    public double getSomaValoresValidos() {
        return somaValoresValidos;
    }

    public List<String> getAnomalias() {
        return anomalias;
    }

    public List<LinhaMovimento> getMovimentosValidos() {
        return movimentosValidos;
    }
}
