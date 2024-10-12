# Simulação de Transmissão de Dados com CRC (Cyclic Redundancy Check)

Este projeto simula a transmissão de dados utilizando o método de verificação de redundância cíclica (CRC) para detecção de erros em mensagens. A simulação envolve dois componentes principais: um transmissor e um receptor, onde dados são enviados entre eles com a verificação e correção de possíveis erros devido ao ruído durante a transmissão.

## Descrição dos Componentes

### 1. **Transmissor**
O transmissor é responsável por converter uma mensagem de texto em uma sequência de bits e calcular o valor do CRC para anexar ao final dessa sequência. O processo de transmissão é simulado com a introdução de ruído aleatório nos dados, e o transmissor envia os dados, incluindo o CRC, ao receptor.

- **Principais Funções do Transmissor:**
  - `streamCaracter`: Converte um caractere em sua representação binária.
  - `calcularBitsMensagemCompleta`: Concatena a mensagem inteira em uma sequência de bits.
  - `calcularCRC`: Realiza o cálculo do CRC com base em um polinômio pré-definido.
  - `geradorRuido`: Introduz ruído aleatório nos dados transmitidos.
  - `enviaDado`: Envia os dados com o CRC e possível ruído ao receptor.

### 2. **Receptor**
O receptor recebe a mensagem transmitida, verifica a validade dos dados utilizando o CRC, e detecta possíveis erros causados por ruído. Caso erros sejam detectados, o receptor pode solicitar a retransmissão da mensagem até um número máximo de tentativas.

- **Principais Funções do Receptor:**
  - `decodificarDado`: Converte a sequência de bits recebida de volta em texto (mensagem original).
  - `decodificarDadoCRC`: Realiza a verificação do CRC nos dados recebidos.
  - `calcularCRC`: Realiza a operação XOR entre os dados e o polinômio CRC para verificar a integridade dos dados.
  - `receberDadoBits`: Recebe a mensagem transmitida, verifica sua integridade e, se necessário, solicita retransmissão.

## Funcionamento do CRC

O CRC (Cyclic Redundancy Check) é um método usado para detectar erros em dados transmitidos. O transmissor calcula um valor de CRC a partir de um polinômio binário (neste caso, 10011) e anexa este valor aos dados antes de enviá-los. O receptor, por sua vez, recalcula o CRC e compara com o valor recebido, identificando se houve erros na transmissão.

## Como Executar

1. Compile e execute o programa.
2. Insira uma mensagem de texto quando solicitado.
3. A simulação irá calcular o CRC, adicionar ruído à mensagem, e transmitir para o receptor.
4. O receptor tentará decodificar a mensagem e verificar sua integridade.
5. Se erros forem detectados, o receptor solicitará a retransmissão até um número máximo de 3 tentativas.

### Exemplo de Execução:

```bash
informe o dado a ser transmitido: Exemplo
Bits adicionando 0's: 010001010111100001101101011100000110110001101111000000000000
Mensagem enviada para check: 010001010111100001101101011100000110110001101111000000000000
Resultado final do CRC: 1001
Resultado da verificação do CRC - Receptor: true
Dados recebidos corretamente: Exemplo
```
## Polinômio Utilizado

O polinômio CRC utilizado no projeto é 10011.

## Detecção de Erros

A cada transmissão, o programa pode introduzir ruído em alguns bits da mensagem. Se o receptor detectar erros durante a transmissão, ele solicita que o transmissor retransmita a mensagem, limitando a 3 tentativas de retransmissão.

## Melhorias Futuras
Implementar técnicas de correção de erros (como Hamming Code) em vez de apenas detectar erros.
Melhorar a eficiência na simulação de ruído e na retransmissão dos dados.

## Autor
Caio Costa
