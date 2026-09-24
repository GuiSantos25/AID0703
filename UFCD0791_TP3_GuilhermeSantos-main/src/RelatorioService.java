import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Responsável exclusivamente pela geração do relatório final de saída,
 * discriminando o resumo do lote, os movimentos válidos e as anomalias.
 */
public class RelatorioService {

    private static final String SEPARADOR = "----------------------------------------";

    /**
     * Gera o ficheiro de relatório. O canal de escrita é sempre fechado no
     * bloco finally, com verificação de não-nulidade.
     *
     * @param caminhoSaida caminho do ficheiro de relatório a criar
     * @param processador  instância que acumulou os resultados do processamento
     * @throws IOException se o ficheiro de saída não puder ser criado ou escrito
     */
    public void gerarRelatorio(String caminhoSaida, ProcessadorMovimentos processador) throws IOException {
        PrintWriter escritor = null;
        try {
            escritor = new PrintWriter(new FileWriter(caminhoSaida));
            escreverCabecalho(escritor);
            escreverResumo(escritor, processador);
            escreverMovimentosValidos(escritor, processador.getMovimentosValidos());
            escreverAnomalias(escritor, processador.getAnomalias());
        } finally {
            if (escritor != null) {
                escritor.close();
            }
        }
    }

    private void escreverCabecalho(PrintWriter escritor) {
        escritor.println("RELATORIO DE PROCESSAMENTO DE MOVIMENTOS BANCARIOS");
        escritor.println(SEPARADOR);
    }

    private void escreverResumo(PrintWriter escritor, ProcessadorMovimentos processador) {
        escritor.println("Total de linhas lidas: " + processador.getTotalLinhasLidas());
        escritor.println("Total de movimentos validos: " + processador.getTotalLinhasValidas());
        escritor.println("Total de anomalias: " + processador.getAnomalias().size());
        escritor.printf("Soma dos valores validos: %.2f%n", processador.getSomaValoresValidos());
        escritor.println(SEPARADOR);
    }

    private void escreverMovimentosValidos(PrintWriter escritor, List<LinhaMovimento> movimentos) {
        escritor.println("MOVIMENTOS VALIDOS:");
        if (movimentos.isEmpty()) {
            escritor.println("(nenhum movimento valido)");
        } else {
            for (LinhaMovimento movimento : movimentos) {
                escritor.println(movimento.toString());
            }
        }
        escritor.println(SEPARADOR);
    }

    private void escreverAnomalias(PrintWriter escritor, List<String> anomalias) {
        escritor.println("ANOMALIAS DETETADAS:");
        if (anomalias.isEmpty()) {
            escritor.println("(nenhuma anomalia detetada)");
        } else {
            for (String anomalia : anomalias) {
                escritor.println(anomalia);
            }
        }
    }
}
