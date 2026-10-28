# Desafio da Telefonia — Sistemas Distribuídos (UFN)

Refatoração em **Java**, na arquitetura **cliente-servidor**, do exercício original de Pesquisa e Ordenação do professor Alexandre Zamberlan. O servidor carrega a base de sessões de telefonia em um `HashMap<String, Sessao>`; o cliente faz consultas a partir de um número de celular pela rede.

## Requisitos

- JDK 17 ou superior (`java` e `javac`)
- Sem dependências externas

## Estrutura

```
src/
  model/Sessao.java          # Classe Sessao do exercício original (adaptada levemente)
  model/TabelaHash.java      # HashMap<String, Sessao>
  utilidades/Util.java       # Leitura do arquivo de sessões
  servidor/Servidor.java     # Servidor TCP multithread
  cliente/Cliente.java       # Cliente TCP de terminal
dados/
  telephony_sessions_exemplo.txt # Duas linhas reais de exemplo da base pública
```

## Compilar (Ubuntu/Linux)

Na pasta raiz do projeto:

```bash
mkdir -p out
javac -encoding UTF-8 -d out $(find src -name '*.java')
```

## Teste imediato com a base de exemplo

**Terminal 1** — servidor:

```bash
java -cp out servidor.Servidor dados/telephony_sessions_exemplo.txt 5000
```

**Terminal 2** — cliente:

```bash
java -cp out cliente.Cliente 127.0.0.1 5000
```

Digite `5592914942603` ou `5569971971316` e pressione Enter. Digite `sair` para encerrar o cliente. Teste também um número inexistente.

## Usar a base completa do professor

Faça download e salve como `dados/telephony_sessions.txt`:

https://github.com/alexandrezamberlan/pesquisa_ordenacao/blob/master/05-hash/problemaSessaoTelefonia/telephony_sessions.txt

No Linux com acesso à internet:

```bash
curl -fL 'https://raw.githubusercontent.com/alexandrezamberlan/pesquisa_ordenacao/master/05-hash/problemaSessaoTelefonia/telephony_sessions.txt' -o dados/telephony_sessions.txt
java -cp out servidor.Servidor dados/telephony_sessions.txt 5000
```

O arquivo oficial é grande; **não** está incluído neste ZIP. Apenas duas linhas públicas de exemplo estão incluídas para permitir o teste imediato.

## Rodar em máquinas diferentes

1. No computador do servidor, inicie `servidor.Servidor` com a porta 5000.
2. Libere a porta TCP 5000 no firewall, se necessário, apenas na rede do laboratório.
3. Descubra o IP do servidor (`hostname -I`).
4. Em outro computador com o projeto compilado, execute `java -cp out cliente.Cliente IP_DO_SERVIDOR 5000`.

## Conceitos de Sistemas Distribuídos

- **Arquitetura cliente-servidor:** cliente requisita, servidor consulta e responde.
- **Sockets TCP:** comunicação confiável orientada à conexão; servidor usa `ServerSocket.accept()` e cada cliente usa `Socket`.
- **Comunicação bloqueante:** `readLine()` espera uma linha na conexão; cada conexão é atendida por uma thread do pool.
- **Concorrência:** `Executors.newFixedThreadPool(10)` permite atender até dez clientes simultaneamente; outros aguardam na fila.
- **HashMap:** indexado pelo número de celular para consulta rápida em memória. É carregado antes das conexões e depois fica somente para leitura, evitando escrita concorrente.
- **Protocolo textual:** cliente envia uma linha contendo número; servidor responde `OK;{dados JSON}` ou `ERRO;mensagem`.

## Observações

- O enunciado **permite escolher a abordagem de cliente-servidor**, usando RMI apenas como exemplo. Esta solução escolheu sockets TCP.
- O arquivo é formatado como `numeroCelular;{JSON da sessão}`. Usa-se `split(";", 2)` para preservar o JSON.
- Se houver números repetidos na base, o HashMap mantém apenas a **última sessão lida** para aquele celular, comportamento coerente com a versão local original (`put`).
- O projeto original usava Swing/NetBeans; esta versão troca a interface por terminal para destacar a comunicação distribuída, sem depender da IDE.

## Referências originais

- Enunciado: https://github.com/alexandrezamberlan/sistemasDistribuidos/blob/master/00-exercicios_trabalhos/7_desafioTelefonia.md
- Código de partida: https://github.com/alexandrezamberlan/pesquisa_ordenacao/tree/master/__aulaPratica/Telefonia
