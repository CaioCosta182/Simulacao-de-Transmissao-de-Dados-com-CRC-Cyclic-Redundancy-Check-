package transferenciadadostp;

import java.util.Arrays;

public class Receptor {

    //mensagem recebida pelo transmissor
    private String mensagem;
    private int tentativasRetransmissao = 0;
    
    public Receptor() {
        //mensagem vazia no inicio da execução
        this.mensagem = "";
    }

    public String getMensagem() {
        return mensagem;
    }

    private boolean decodificarDado(boolean bits[]) {
        int codigoAscii = 0;
        int expoente = bits.length - 1;

        //converntendo os "bits" para valor inteiro para então encontrar o valor tabela ASCII
        for (int i = 0; i < bits.length; i++) {
            if (bits[i]) {
                codigoAscii += Math.pow(2, expoente);
            }
            expoente--;
        }

        //concatenando cada simbolo na mensagem original
        this.mensagem += (char) codigoAscii;

        //esse retorno precisa ser pensado... será que o dado sempre chega sem ruído???
        return true;
    }

private void decodificarDadoCRC(boolean[] bits) {
    // Supondo que a mensagem recebida inclui tanto os dados quanto os bits de CRC no final

    // Obtém o polinômio CRC do transmissor
    boolean[] polinomioCRC = Transmissor.getPolinomioCRC();
    int tamanhoCRC = polinomioCRC.length;

  //  System.out.println("Dados recebidos corretamente: " + Arrays.toString(bits));
    // Tamanho dos dados é o total de bits menos o tamanho do polinômio CRC
    int tamanhoDados = bits.length - tamanhoCRC;

    // Obtém os dados e os bits de CRC
    boolean[] dadosRecebidos = Arrays.copyOfRange(bits, 0, tamanhoDados);
    boolean[] bitsCRC = Arrays.copyOfRange(bits, tamanhoDados, bits.length);

    // Verifica se há erros nos dados recebidos
    boolean indicadorCRC = calcularCRC(dadosRecebidos, bitsCRC);

    if (indicadorCRC) {
        // Se o CRC indicar que não há erros, então os dados recebidos são válidos
       decodificarDado(dadosRecebidos);
        System.out.println("Dados recebidos corretamente:"+ mensagem);
        
       
    } else {
        // Se o CRC indicar que há erros, você pode implementar um mecanismo de retransmissão
        // ou simplesmente descartar os dados recebidos.

        System.out.println("Erro de transmissão detectado. Os dados recebidos estão corrompidos.");
         System.out.println("Solicitando reenvio dos dados...");

        if (tentativasRetransmissao < 3) {
            tentativasRetransmissao++; // Incrementa o número de tentativas de retransmissão
            // Realiza a retransmissão
            receberDadoBits(bits);
        } else {
            // Se exceder o número máximo de tentativas de retransmissão
            System.out.println("Número máximo de tentativas de retransmissão excedido. Falha na recepção dos dados.");
        }
    }
}


    private boolean calcularCRC(boolean[] dados, boolean[] bitsCRC) {
        // Inicializa o resultado do CRC com os dados recebidos
        boolean[] resultadoCRC = Arrays.copyOf(dados, dados.length);

        // Obtém o polinômio CRC do transmissor
        boolean[] polinomioCRC = Transmissor.getPolinomioCRC();

        
        // Loop para dividir os dados pelo polinômio CRC
        for (int i = 0; i < dados.length; i++) {
            System.out.println("Receptor - Resultado parcial do CRC no passo " + i + ": " + arrayToString(resultadoCRC));
            // Verifica se o bit mais significativo é 1
            if (resultadoCRC[i]) {
                // Realiza a operação XOR com o polinômio CRC
                for (int j = 0; j < polinomioCRC.length; j++) {
                    // Verifica se ainda há bits no resultado do CRC para realizar a operação XOR
                    if (i + j < resultadoCRC.length) {
                        System.out.println("XOR entre " + (resultadoCRC[i + j] ? "1" : "0") + " e " + (polinomioCRC[j] ? "1" : "0"));
                        resultadoCRC[i + j] ^= polinomioCRC[j];
                    }
                }
            }
        }

        boolean indicadorCRC = Arrays.equals(resultadoCRC, bitsCRC);
        System.out.println("------------------------------------------------------------------------------ " );
        System.out.println("Resultado da verificação do CRC - Receptor: " + indicadorCRC);
        return indicadorCRC;
    }

 
    //recebe os dados do transmissor
    public boolean receberDadoBits(boolean bits[]) {
        //aqui você deve trocar o método decodificarDado para decodificarDadoCRC (implemente!!)
        decodificarDadoCRC(bits);
        //será que sempre teremos sucesso nessa recepção?
        return true;
    }

    // Método para converter um array de boolean para uma string representativa
    private static String arrayToString(boolean[] array) {
        StringBuilder builder = new StringBuilder();
        for (boolean bit : array) {
            builder.append(bit ? "1" : "0");
        }
        return builder.toString();
    }

}