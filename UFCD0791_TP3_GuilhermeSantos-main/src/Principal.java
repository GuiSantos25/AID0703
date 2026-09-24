import java.io.IOException;

/**
 * Ponto de entrada da aplicação de consola de processamento resiliente
 * de lotes de movimentos bancários.
 */
public class Principal {

    private static final int NUMERO_ARGUMENTOS_ESPERADO = 2;
    private static final int INDICE_ARG_ENTRADA = 0;
    private static final int INDICE_ARG_SAIDA = 1;

    /**
     * Recebe via linha de comandos o caminho do ficheiro de entrada e o
     * caminho do ficheiro de relatório de saída, e orquestra o ciclo
     * completo de processamento.
     *
     * @param args {@code args[0]} caminho do ficheiro de movimentos,
     *             {@code args[1]} caminho do ficheiro de relatório
     */
    public static void main(String[] args) {
        if (args.length != NUMERO_ARGUMENTOS_ESPERADO) {
            System.err.println("Uso: java -cp bin Principal <caminho/para/movimentos.txt> <caminho/para/relatorio.txt>");
            return;
        }

        String caminhoEntrada = args[INDICE_ARG_ENTRADA];
        String caminhoSaida = args[INDICE_ARG_SAIDA];

        try {
            executarProcessamento(caminhoEntrada, caminhoSaida);
        } catch (IOException erroIO) {
            System.err.println("Erro de I/O durante o processamento: " + erroIO.getMessage());
            System.err.println("Verifique se o ficheiro de entrada existe e se o caminho de saida e valido.");
        }
    }

    private static void executarProcessamento(String caminhoEntrada, String caminhoSaida) throws IOException {
        ProcessadorMovimentos processador = new ProcessadorMovimentos();
        processador.processarFicheiro(caminhoEntrada);

        RelatorioService relatorioService = new RelatorioService();
        relatorioService.gerarRelatorio(caminhoSaida, processador);

        System.out.println("Processamento concluido.");
        System.out.println("Linhas lidas: " + processador.getTotalLinhasLidas());
        System.out.println("Movimentos validos: " + processador.getTotalLinhasValidas());
        System.out.println("Anomalias: " + processador.getAnomalias().size());
        System.out.println("Relatorio gerado em: " + caminhoSaida);
    }
}
