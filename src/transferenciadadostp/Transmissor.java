package transferenciadadostp;

import java.util.Random;

public class Transmissor {

    private final String mensagem;
    private final boolean[] polinomio = {true, false, false, true, true};  // Representação binária de 10011

    // Método auxiliar para obter o polinômio do transmissor
    static boolean[] getPolinomioCRC() {
        return new boolean[]{true, false, false, true, true};
    }

    private final boolean[] polinomioNulo = {false, false, false, false, false};
    private final int grauPolinomio = polinomio.length - 1;
    private boolean[] bitsMensagem;
    private boolean[] bitsCRC;

    public Transmissor(String mensagem) {
        this.mensagem = mensagem;
    }

    //convertendo um símbolo para "vetor" de boolean (bits)
    private boolean[] streamCaracter(char simbolo) {

        //cada símbolo da tabela ASCII é representado com 8 bits
        boolean bits[] = new boolean[8];

        //convertendo um char para int (encontramos o valor do mesmo na tabela ASCII)
        int valorSimbolo = (int) simbolo;
        int indice = 7;

        //convertendo cada "bits" do valor da tabela ASCII
        while (valorSimbolo >= 2) {
            int resto = valorSimbolo % 2;
            valorSimbolo /= 2;
            bits[indice] = (resto == 1);
            indice--;
        }
        bits[indice] = (valorSimbolo == 1);

        return bits;
    }

    private void calcularBitsMensagemCompleta() {
        this.bitsMensagem = new boolean[this.mensagem.length() * 8];
        int indiceMensagemCompleta = 0;

        for (int i = 0; i < this.mensagem.length(); i++) {
            boolean charBits[] = streamCaracter(this.mensagem.charAt(i));

            // Adiciona os bits do caractere atual à mensagem completa
            System.arraycopy(charBits, 0, bitsMensagem, indiceMensagemCompleta, charBits.length);
            indiceMensagemCompleta += charBits.length;
        }
        System.out.println("Função concatenar BitsMensagem (sem zeros): " + arrayToString(this.bitsMensagem));
    }

    private boolean[] dadoBitsCRC(boolean[] bitsMensagem) {
        // Primeiro, calculamos os bits da mensagem completa
        calcularBitsMensagemCompleta();

        // Criando uma cópia estendida dos bits originais com espaço para o CRC
        boolean bitsCRC[] = new boolean[bitsMensagem.length + grauPolinomio];

        // Copia os bits originais para a parte estendida
        for (int i = 0; i < bitsMensagem.length; i++) {
            bitsCRC[i] = bitsMensagem[i];
        }
        System.out.println("Bits  adicionando 0's: " + arrayToString(bitsCRC));
        return bitsCRC;
    }

    //não modifique (seu objetivo é corrigir esse erro gerado no receptor)
    private void geradorRuido(boolean bits[]) {
        Random geradorAleatorio = new Random();

        //pode gerar um erro ou não..
        if (geradorAleatorio.nextInt(5) > 1) {
            int indice = geradorAleatorio.nextInt(8);
            bits[indice] = !bits[indice];
        }
    }

    public boolean[] calcularCRC(boolean[] bitsCRC) {
        // Primeiro, invocamos a função que calcula os bits da mensagem completa e adiciona os zeros
        bitsCRC = dadoBitsCRC(bitsMensagem);
        System.out.println("Mensagem enviada para check: " + arrayToString(bitsCRC));

        boolean[] resultadoCRC = new boolean[polinomio.length];
        boolean[] finalCRC = new boolean[grauPolinomio];

        for (int i = 0; i < resultadoCRC.length; i++) {
            resultadoCRC[i] = bitsCRC[i];
        }

        System.out.println("Resultado parcial do CRC no passo 0: " + arrayToString(resultadoCRC));

        // Loop externo para percorrer a mensagem completa + zeros
        for (int i = 0; i < bitsCRC.length - grauPolinomio; i++) {
            System.out.println("Bit atual do CRC é 1 no passo " + i);
            System.out.println("Resultado parcial do CRC no passo " + i + ": " + arrayToString(resultadoCRC));

            // Loop interno para realizar XOR entre resultadoCRC (resultado XOR anterior) e polinomio
            if (resultadoCRC[0] == true) {
                for (int j = 0; j < polinomio.length; j++) {
                    System.out.println("XOR entre " + (resultadoCRC[j] ? "1" : "0") + " e " + (polinomio[j] ? "1" : "0"));
                    resultadoCRC[j] = resultadoCRC[j] ^ polinomio[j];

                }
            }

            //Loop para recuperar proximo bit da mensagem transferida
            for (int indice = 0; indice < resultadoCRC.length - 1; indice++) {
                resultadoCRC[indice] = resultadoCRC[indice + 1];
            }

            //condição para encerrar quando o ultimo bit for recuperado
            if (i != bitsCRC.length - grauPolinomio - 1) {
                resultadoCRC[4] = bitsCRC[resultadoCRC.length + i];
            } else {
                for (int k = 0; k < finalCRC.length; k++) {
                    finalCRC[k] = resultadoCRC[k];
                }
            }
        }
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Resultado final do CRC: " + arrayToString(finalCRC));
        System.out.println("--------------------------------------------------------------------------");
        return finalCRC;
    }

    public void enviaDado(Receptor receptor) {
        calcularBitsMensagemCompleta();

        // Calcule o CRC corretamente
        boolean finalCRC[] = calcularCRC(bitsCRC);

        // Concatene finalCRC com bitsMensagem
        boolean bits[] = new boolean[bitsMensagem.length + finalCRC.length];
        System.arraycopy(finalCRC, 0, bits, bitsMensagem.length, finalCRC.length);
        System.arraycopy(bitsMensagem, 0, bits, 0 , bitsMensagem.length);

        // Adicione ruído aos bits da mensagem
        geradorRuido(bits);

        //enviando a mensagem "pela rede" para o receptor (uma forma de testarmos esse método)
        // Envie a mensagem com CRC para o receptor
        boolean indicadorCRC = receptor.receberDadoBits(bits);
       System.out.println("Resultado para enviar bits: " + arrayToString(bits));
       
        //o que faremos com o indicador quando houver algum erro? 
        //qual ação vamos tomar com o retorno do receptor
        // Imprima informações de depuração
        System.out.println("Indicador CRC - Transmissor: " + indicadorCRC);
        System.out.println("--------------------------------------------------------------------------");
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