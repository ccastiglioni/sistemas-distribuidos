# Cadastro de clientes e tokens via UDP

A aplicação implementa os requisitos de [exercicios.md](exercicios.md):

- interface Java Swing para informar nome completo e e-mail;
- envio e recebimento UDP em UTF-8 pela classe `Comunicador`;
- lista de objetos `Pessoa` mantida na memória do servidor;
- bloqueio de cadastros com o mesmo e-mail, ignorando maiúsculas e espaços nas extremidades;
- validação dos dados e resposta para mensagens inválidas.
- token aleatório individual, válido por exatamente 60 segundos;
- consultas automáticas do token a cada 10 segundos, além da consulta manual.

## Executar

Use Java 17 ou superior. Dentro de `aula-6-UDP`, compile:

```bash
mkdir -p /tmp/aula-6-udp-classes
javac -encoding UTF-8 -d /tmp/aula-6-udp-classes Pessoa.java Comunicador.java ServidorUDP.java ClienteUDP.java
```

Inicie o servidor em um terminal:

```bash
java -cp /tmp/aula-6-udp-classes ServidorUDP
```

Em outro terminal, inicie o cliente em um ambiente com interface gráfica:

```bash
java -cp /tmp/aula-6-udp-classes ClienteUDP
```

O endereço local (`127.0.0.1`), a porta (`12345`), o limite da mensagem
(`1024` bytes) e o tempo limite da resposta (`5` segundos) estão definidos em
`Comunicador`. O cadastro usa o formato `nome;e-mail`; por isso, os
campos não aceitam ponto e vírgula. Ele exige nome com ao menos duas
partes e e-mail com usuário, arroba e domínio com ponto.

O cliente espera a resposta em uma tarefa de fundo para manter a janela
responsiva. Como UDP pode perder mensagens, se uma resposta não chegar, tente
novamente: se o servidor já tiver recebido o cadastro, ele informará que o e-mail
está cadastrado e o cliente iniciará as consultas do token. Os cadastros e tokens
são perdidos quando o servidor é encerrado.

## Funcionamento do token

Após a confirmação de cadastro, inclusive quando o e-mail já existe, o cliente
solicita imediatamente o token e repete a consulta a cada 10 segundos. O botão
**Solicitar token** também permite consultar a chave a qualquer momento. As
solicitações não se sobrepõem e a espera pela rede ocorre fora da thread gráfica.

O servidor gera um UUID aleatório na primeira solicitação e registra seu
vencimento, 60 segundos depois, na mesma `Pessoa`. Antes desse instante, devolve
a mesma chave e o mesmo vencimento. No instante exato do vencimento ou depois,
gera uma nova chave com um novo prazo de 60 segundos. Consultar um token válido
nunca estende seu prazo.

A interface mostra a chave e o horário de vencimento informado pelo servidor.
Quando vence, a chave desaparece da tela, mesmo se a rede estiver indisponível.
O cliente continua tentando consultar periodicamente após falhas de comunicação.
Se o servidor informar que o cadastro não existe (por exemplo, após reiniciar),
as consultas param e o usuário deve cadastrar-se novamente. Ao iniciar outro
cadastro ou fechar a janela, as consultas anteriores são interrompidas.

O servidor usa seu próprio relógio para controlar a validade; o horário exibido
na interface depende também do relógio local do cliente. O prazo fica definido
em `Pessoa.VALIDADE_TOKEN` e o intervalo em `ClienteUDP.INTERVALO_SOLICITACAO_TOKEN`.

### Mensagens UDP

| Operação | Solicitação | Resposta |
| --- | --- | --- |
| Cadastro | `nome;e-mail` | Confirmação, aviso de duplicidade ou erro de validação |
| Consulta do token | `TOKEN;e-mail` | `TOKEN;chave;vencimento` |

O vencimento é um instante Unix em milissegundos. A consulta com e-mail sem
cadastro recebe um aviso para realizar o cadastro, sem criar um usuário.

## Conferir o cadastro

1. Cadastre `João da Silva` com `joao@example.com`: deve aparecer uma confirmação.
2. Envie outro nome com ` JOAO@example.com `: deve informar cadastro duplicado.
3. Cadastre outra pessoa com e-mail diferente: deve ser aceita.
4. Informe nome ou e-mail vazio ou inválido: o cliente deve explicar o erro.
5. Encerre o servidor e tente cadastrar: a interface deve continuar respondendo
   e informar a falha de comunicação.

## Conferir os tokens

1. Cadastre-se e confira se a chave aparece automaticamente.
2. Use **Solicitar token** antes do vencimento: a chave e o horário devem permanecer
   iguais. As consultas automáticas também devem manter esses valores.
3. Aguarde o vencimento e a próxima consulta (ou use o botão): deve aparecer uma
   chave diferente, válida por mais 60 segundos.
4. Abra outro cliente com e-mail diferente: cada pessoa deve ter sua própria chave.
5. Abra outro cliente com o mesmo e-mail: ambos devem receber a mesma chave enquanto
   ela estiver válida, sem duplicar o cadastro.
6. Pare o servidor: a interface deve continuar respondendo, informar a falha e
   apagar a chave no vencimento. Reinicie o servidor e cadastre-se novamente
   quando o cliente informar que o cadastro não existe mais.
