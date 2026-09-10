# Resumo do repositório do professor (Sistemas Distribuídos)

> Gerado a partir da leitura do repositório local `/var/www/html/sistemasDistribuidos` (clone de
> `github.com/alexandrezamberlan/sistemasDistribuidos`), **sem alterar nenhum arquivo dele**.
> Conteúdo em Python foi propositalmente desconsiderado nos resumos abaixo, conforme pedido.

## 1. Sobre o repositório

- **Professor:** Alexandre de O. Zamberlan (`alexz@ufn.edu.br` / `alexandre.o.zamberlan@gmail.com`) — UFN, disciplina de Sistemas Distribuídos (curso de Ciência da Computação, também citado Sistemas de Informação).
- **Remote:** `https://github.com/alexandrezamberlan/sistemasDistribuidos.git`, branch única `master` (histórico linear, sem PRs/branches de feature).
- **Licença:** GPLv3.
- **Linguagens:** Java (maioria), C#, Python (ignorado aqui) — o próprio README diz: *"Repositório com códigos trabalhados na disciplina SD: threads, sockets, rpc, ... tanto em Java, Python, quanto C#"*.
- **174 commits** no total, `Initial commit` até o commit mais recente. Muitos commits são feitos diretamente de contas de laboratório da UFN (`Laboratório 01/06/15/25 @lanfran.local`), ou seja, boa parte do código é escrito/commitado durante a própria aula prática.
- O repositório é **reaproveitado entre semestres**: há pastas com sufixo `2025`, provas antigas de 2024, e o arquivo de diário de aula (`0_quadroBranco.md`) mistura conteúdo de um semestre anterior com o semestre atual (ver seção 4).

## 2. Estrutura de pastas

| Pasta | Tema | Linguagem(ns) | Conteúdo |
|---|---|---|---|
| `0_quadroBranco.md` | Diário de aula | — | Registro semana a semana do que foi dado/pedido (fonte principal da seção 4) |
| `0-desafiosAvaliacoesAntigas/` | Atividades e prova antiga | Python (ignorado) | `atividades.md` (3 tarefas de threads), `prova1_SD.md` (prova 2024) |
| `1_resumoSistemasDistribuidos.md`, `2_arquiteturas.md`, `3_comunicacao.md`, `4_exercicios.md` | Teoria condensada | — | Conceitos de SD, arquiteturas, comunicação/sincronismo, exercícios de divisão-e-conquista com threads |
| `1-Colecoes/` | Revisão de POO/Coleções | Java | Exemplo bancário (Conta, ContaCorrente, ContaPoupança, Correntista) — não é conteúdo específico de SD, parece revisão de base |
| `2-Threads/` | Threads | Java, C# (Python ignorado) | Exemplos com/sem memória compartilhada, jogos usados como exercício de concorrência (Jogo da Frutinha, TeleJogo, jogo da cobrinha); arquivos `2_threadsEm_java_csharp_python.md` e `3_exemplosCodigos_java_csharp_python.md` com exemplos comparativos |
| `3-Sockets/` e `3.1-Sockets_Swing/` | Sockets TCP/UDP | Java, C# | Exemplos básicos (`exemplo1/2/3`), geração de e-mail via socket, TCP x UDP com/sem classe `Comunicador`, versão com interface gráfica (Swing) |
| `4-ThreadSocketCobrinha/` | Sockets + Threads | Java | Jogo da cobrinha cliente-servidor (`ClienteJogador2`, `ServidorJogador1`), usa serialização via `ObjectOutputStream`/`ObjectInputStream` |
| `5-RPC_Python/` | RPC | Python (ignorado) | Apenas PDFs de apresentação — conteúdo não detalhado aqui |
| `6-RMI-Java/` | Remote Method Invocation | Java | Vários exemplos (`exemplo`, `exemplo2`, `RMI Aula`, `RMI CallBack`, `JogoFrutinha_RMI`, `TrabalhoRMI`) |
| `7-Multicast/` | Multicast | Java (pasta `python/` ignorada) | Exemplos `ex1/2/3`, trabalho de chat multicast em turma, resumo teórico em `resumoMulticast.txt` |
| `8-JGroups/` | Multicast confiável via biblioteca JGroups | Java | Lib `jgroups-4.2.4.Final.jar`, exemplos (`JGroups`, `JGroups_JogoFrutinha`, `JGroups_jogoVelha`) e trabalhos de alunos |
| `9 - UsandoJSON/` | Serialização JSON | Java (Gson), Python (ignorado) | Exemplo de uso de JSON como formato de troca de dados |
| `ChatSocket_ClienteServidor/` | Chat cliente-servidor | Java (Swing) | `Comunicador.java`, telas `JFrame_Cliente`/`JFrame_Servidor` |
| `provas/prova1.md` | Prova (gabarito comentado) | Python (código ignorado, teoria mantida) | Teoria de SD + exercícios práticos de threads (ver seção 6) |
| `trabalhosAlunos/PongSockets/` | Trabalho de aluno | Java | Jogo Pong via sockets |
| `sistemasDistribuidos.sln` | Solution Visual Studio | C# | Agrupa os projetos C# (`3-Sockets/C#`, `2-Threads/2025/.../csharp`) |

## 3. Linha do tempo do git (o que dá pra observar no histórico)

- Sem branches de feature: tudo direto na `master`.
- Autoria mistura o professor (3 e-mails diferentes ao longo do tempo) com contas de laboratório da universidade — sinal de que muito código é escrito ao vivo, em aula, na máquina do laboratório.
- **Semestre atual (2026) identificado pelas datas dos commits:**
  - `4b82e1a` / `a5824e6` — 29–31/07/2026 — commit `"aula 1"`
  - `44f835f` — 05/08/2026 — `"exercicios"` (exercícios de threads)
  - `d0bc432`, `62ca88f`, `5053d31`, `929e215` — 07/08/2026 — `"quadro branco aula semana 2"` + ajustes
  - `419191a` — 07/08/2026 — `"exercicios de threads"`
  - `35958ea` — 12/08/2026 — `"aula 3"` (atualiza quadro branco e exemplos de threads)
  - `52ea4a7` (**mais recente**) — 12/08/2026 — `"exemplos de clientes"` — adiciona exemplos de **cliente** Java/C# em `2-Threads/3_exemplosCodigos_java_csharp_python.md` (servidor multithread com socket + cliente correspondente nas 3 linguagens)
- **Importante:** o `0_quadroBranco.md` tem conteúdo de "Semana 6" até "Semana 18" (avaliação, sockets, RPC/RMI, multicast, JGroups) que **não tem commit recente correspondente** — ou seja, é conteúdo remanescente de uma edição/semestre anterior, deixado no arquivo como o roteiro que a disciplina tende a seguir mais adiante. O que está de fato "quente" agora (commitado nas últimas semanas) é só até a **Semana 3/4 (Threads)**.

## 4. Cronologia de aulas (via `0_quadroBranco.md`, reorganizado em ordem crescente)

| Semana | Conteúdo |
|---|---|
| 1 | Apresentação do plano de ensino. Avaliação: **20% participação, 20% notas de aula (arquivo `notas_aula.md` no github pessoal), 60% provas/trabalhos**. Conceitos básicos: comunicação (broadcast/multicast/unicast, bloqueante, modelo TCP/IP), arquiteturas (cliente-servidor, ponto-a-ponto) |
| 2 | Revisão (SD, comunicação, TCP/IP, arquiteturas). Threads nas 3 linguagens (visão geral). **Desafio 1: Divisão e Conquista** |
| 3 | Threads: o que são, para que servem, quando usar/não usar, tipos (com/sem memória compartilhada). Exercício: identificar threads em execução/finalizadas. Desafios: ler `numeros.txt`/`nomes.txt` com threads sem memória compartilhada; ler `numeros1.txt`+`numeros2.txt` populando a mesma lista com threads **com** memória compartilhada |
| 4 | Entrega do desafio 2. Revisão de passagem de parâmetro (Java/C#/Python). Quando threads **não devem** ser usadas. Threads **com** memória compartilhada |
| 5 | Sincronização distribuída: relógios físicos e lógicos (Lamport), exclusão mútua, eleição. Grid (concomitância) x Cluster (paralelismo). Pool de threads. Atividade: pesquisar e publicar no github pessoal sobre relógios/exclusão mútua/eleição e sobre pool de threads; refazer o TeleJogo com threads (com e sem recurso compartilhado) |
| 6 | Avaliação (prova) |
| 7 | Correção/discussão da prova + introdução a Sockets (camada TCP/IP, endereço, porta, estrutura cliente-servidor clássica) |
| 8 | Sockets em Java + modo gráfico (chat onde ambos os lados leem/escrevem; servidor como "prestador de serviço": conecta → recebe → processa → devolve → fecha → recomeça) |
| 9 | Sockets com classe `Comunicador`, diferença TCP x UDP. Atividade avaliativa: refatorar o exemplo "gerar e-mail" incluindo modo gráfico e classe `Comunicador` |
| 10 | Socket UDP. Adaptar o jogo da cobrinha (pasta `4-ThreadSocketCobrinha`) — requisitos diferentes para Ciência da Computação x Sistemas de Informação. Gerador de código estilo Google Authenticator (sorteio de frutas) |
| 11 | RPC/RMI: cliente sem poder computacional pede serviço a um servidor via interface. Java = RMI, Python = RPC, C# = xmlRPC. Exercício: serviço que recebe nome completo e devolve e-mail (`nome.sobrenome@ufn.edu.br`) e outro que devolve hash |
| *(12–14 sem registro no quadro atual)* | — |
| 15 | Multicast em Java: arquitetura ponto-a-ponto, protocolo UDP, IP de grupo (faixa `239.x.y.w`), thread "ouvidora/receptora" + "falante/enviadora" |
| 16 | JGroups — desafio (2 pts) de melhorias no `8-JGroups/JGroups`: mostrar IP ao lado dos membros, notificar saída de membro, atualizar mensagens para quem entra atrasado |
| 17 | JGroups — apresentação de trabalhos |
| 18 | Atividade de recuperação: usando RPC ou RMI, receber uma string de fundamentos do padel (`nomeFundamento;zona;resultado`) e retornar um objeto `Jogada` |

## 5. Avaliação da disciplina

- **20%** participação efetiva em aula
- **20%** notas de aula — o aluno deve manter um arquivo **`notas_aula.md`** no próprio github pessoal
- **60%** provas + trabalhos práticos

> ⚠️ Não encontrei `notas_aula.md` neste repositório (`sistemas-distribuidos`) — vale criar, já que vale 20% da nota.

## 6. O que já caiu de prova (repositório do professor)

**`0-desafiosAvaliacoesAntigas/prova1_SD.md` (prova 2024):** cliente-servidor x P2P, SD x sistemas paralelos, sincronização (relógio lógico x físico), exclusão mútua, escalabilidade, locks/deadlock, papel das threads, exercício de identificar pontos de paralelismo em um código de leitura/ordenação/escrita de arquivos, e uma questão sobre a LGPD.

**`provas/prova1.md` (gabarito comentado):** mesma base teórica (SD, transparência, síncrono x assíncrono, arquiteturas, tipos de falha, relógios físicos/lógicos, algoritmo de Lamport, memória compartilhada x troca de mensagens, escalabilidade) mais uma bateria de exercícios práticos de threads — pares/ímpares, condição de corrida sem lock x com lock, produtor-consumidor, relógio lógico simplificado, `Barrier`, deadlock (e como evitar: ordem consistente de aquisição de locks), servidor multi-thread com socket.

## 7. Leitura para hoje (19/08/2026)

Pelo ritmo dos commits (aula 1 em 29–31/07, aula 2 em 07/08, aula 3 em 12/08 — uma aula nova a cada ~1 semana), hoje provavelmente cai na **Semana 4** do quadro branco: revisão de passagem de parâmetro nas 3 linguagens, threads **com memória compartilhada**, e possivelmente a entrega do "desafio 2" (os exercícios de `numeros.txt`/`nomes.txt` da Semana 3). Isso é inferência a partir do histórico, não uma confirmação — vale validar com o professor/colegas antes da aula.

## 8. Sobre o Python (desconsiderado)

Como pedido, o conteúdo em Python não foi detalhado. Pastas/arquivos com Python presentes no repositório do professor: `5-RPC_Python/`, `7-Multicast/python/`, `9 - UsandoJSON/python/`, e os trechos em Python de `2-Threads/3_exemplosCodigos_java_csharp_python.md` e `provas/prova1.md`.
