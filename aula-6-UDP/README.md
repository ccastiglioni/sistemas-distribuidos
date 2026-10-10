# Cadastro de clientes via UDP — etapa 1

Esta etapa implementa o cadastro pedido em [exercicios.md](exercicios.md):

- interface Java Swing para informar nome completo e e-mail;
- envio e recebimento UDP em UTF-8 pela classe `Comunicador`;
- lista de objetos `Pessoa` mantida na memória do servidor;
- bloqueio de cadastros com o mesmo e-mail, ignorando maiúsculas e espaços nas extremidades;
- validação dos dados e resposta para mensagens inválidas.

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
`Comunicador`. A troca de mensagens usa o formato `nome;e-mail`; por isso, os
campos não aceitam ponto e vírgula. O cadastro exige nome com ao menos duas
partes e e-mail com usuário, arroba e domínio com ponto.

O cliente espera a resposta em uma tarefa de fundo para manter a janela
responsiva. Como UDP pode perder mensagens, se uma resposta não chegar, tente
novamente: se o servidor já tiver recebido o cadastro, ele informará que o e-mail
está cadastrado. Os cadastros são perdidos quando o servidor é encerrado.

## Conferir o cadastro

1. Cadastre `João da Silva` com `joao@example.com`: deve aparecer uma confirmação.
2. Envie outro nome com ` JOAO@example.com `: deve informar cadastro duplicado.
3. Cadastre outra pessoa com e-mail diferente: deve ser aceita.
4. Informe nome ou e-mail vazio ou inválido: o cliente deve explicar o erro.
5. Encerre o servidor e tente cadastrar: a interface deve continuar respondendo
   e informar a falha de comunicação.

## Segunda etapa

Ficam para o próximo commit a geração de token aleatório por cliente, a validade
estrita de 60 segundos, a reutilização enquanto válido, a renovação quando
expirado e as solicitações periódicas pelo cliente.
