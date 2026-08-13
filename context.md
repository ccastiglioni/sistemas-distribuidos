# Sistemas Distribuídos — Contexto da disciplina

Este repositório reúne exemplos acadêmicos da disciplina de Sistemas Distribuídos. Os exercícios serão organizados nas pastas `aula1` até `aula8`.

## Fundamentos

Sistemas distribuídos são formados por máquinas heterogêneas, que podem possuir diferentes arquiteturas de hardware, sistemas operacionais e linguagens de programação. Essas máquinas são fracamente acopladas e podem estar geograficamente distribuídas, comunicando-se por protocolos do modelo TCP/IP.

A comunicação considera elementos como endereço de rede, porta lógica, máscara de rede e protocolo de transporte. Na essência, ela ocorre por sockets, com IP, porta e objetos de leitura e escrita. Como as operações de leitura e escrita podem ser bloqueantes, threads podem ser utilizadas para permitir o processamento concomitante.

Entre os principais aspectos de um sistema distribuído estão:

- compartilhamento de recursos, como processador e memória;
- tolerância a falhas;
- escalabilidade;
- segurança;
- manutenção e atualização;
- heterogeneidade;
- comunicação entre máquinas;
- sincronização de tempo e de acesso a recursos.

Um exemplo de infraestrutura distribuída é o grid computacional.

## Sistemas distribuídos e sistemas paralelos

Sistemas distribuídos geralmente são heterogêneos, fracamente acoplados e geograficamente distribuídos. Podem adotar arquiteturas cliente-servidor, ponto a ponto ou híbridas.

Sistemas paralelos geralmente são homogêneos, fortemente acoplados e mantidos em um mesmo local. Um exemplo é o cluster computacional. Seu objetivo também envolve o compartilhamento de recursos de processamento e memória.

## Arquiteturas

### Cliente-servidor

É um modelo centralizado no qual uma ou mais máquinas atuam como servidores. Os clientes solicitam serviços, dados ou recursos, enquanto os servidores gerenciam e respondem às requisições.

Características:

- papéis distintos entre cliente e servidor;
- administração centralizada;
- comunicação baseada em requisição e resposta;
- capacidade limitada pelos recursos do servidor;
- possível ponto único de falha quando não há redundância.

Um navegador acessando um servidor web é um exemplo de cliente-servidor.

### Ponto a ponto (P2P)

É um modelo descentralizado no qual os nós possuem papéis equivalentes. Cada nó pode solicitar e fornecer recursos diretamente a outros nós, atuando como cliente e servidor.

Características:

- comunicação direta entre pares;
- descentralização;
- maior distribuição de recursos;
- escalabilidade;
- resiliência diante da falha de um nó.

BitTorrent e redes blockchain são exemplos de sistemas ponto a ponto.

### Arquitetura híbrida

Combina características das arquiteturas cliente-servidor e P2P. Um serviço central pode ser usado para descoberta, autenticação ou coordenação, enquanto a troca principal de dados ocorre diretamente entre os pares.

### Comparação resumida

| Aspecto | Cliente-servidor | Ponto a ponto |
| --- | --- | --- |
| Estrutura | Centralizada | Descentralizada |
| Papel dos nós | Servidor fornece e cliente consome | Todos podem fornecer e consumir |
| Comunicação | Cliente com servidor | Nó diretamente com outro nó |
| Ponto único de falha | Pode existir no servidor | Não há um único nó central obrigatório |
| Escalabilidade | Limitada pela infraestrutura central | Recursos são distribuídos entre os nós |
| Exemplos | Web e bancos de dados | BitTorrent e blockchain |

## Comunicação entre máquinas

Os nós de um sistema distribuído estão fisicamente separados e conectados por uma rede. Eles precisam trocar mensagens para compartilhar dados, coordenar ações e cooperar na execução de tarefas.

A comunicação pode utilizar TCP/IP, HTTP, RPC e outros protocolos. Seus principais desafios são:

- latência e largura de banda;
- perda, duplicação ou atraso de mensagens;
- chegada de mensagens fora de ordem;
- falhas ou lentidão dos nós;
- heterogeneidade dos sistemas;
- bloqueio durante operações de leitura e escrita.

## Sincronização

O sincronismo coordena processos e nós para evitar conflitos, manter a consistência dos dados e garantir uma ordem válida dos eventos.

Existem duas necessidades principais:

- sincronismo temporal: utiliza relógios físicos ou lógicos para relacionar e ordenar eventos;
- sincronismo de ações e recursos: utiliza espera por respostas, barreiras, monitores, semáforos, locks ou exclusão mútua.

A sincronização distribuída é complexa porque não existe um relógio global perfeitamente sincronizado, as mensagens podem sofrer atrasos ou perdas e os nós podem falhar. Relógios lógicos de Lamport, algoritmos de consenso como Paxos e Raft e mecanismos de exclusão mútua distribuída são soluções relacionadas a esses problemas.

## Threads

Uma thread é uma unidade de execução pertencente a um processo. Ela permite que diferentes tarefas sejam executadas concomitantemente dentro da aplicação. Uma thread pode passar por estados como criada, pronta, em execução, aguardando, dormindo, interrompida ou finalizada.

Threads podem trabalhar:

- com memória ou recursos compartilhados, caso em que o programador deve garantir o sincronismo;
- sem compartilhamento de memória ou recursos, mantendo o processamento de cada tarefa isolado.

Em Java, threads podem ser implementadas estendendo a classe `Thread` ou fornecendo uma tarefa por meio da interface `Runnable`. A escolha deve considerar o compartilhamento de estado e o desenho da solução, não apenas a forma de criar a thread.

## Exercícios

### 1. Divisão e conquista: soma de sublistas

Contexto: processamento de grandes volumes de dados numéricos.

Problema: gerar uma lista ou vetor com 10.000 números inteiros aleatórios e dividi-lo em quatro partes iguais.

Ação: criar quatro threads. Cada thread recebe somente uma parte, calcula a soma dos elementos de sua sublista e disponibiliza o resultado parcial.

Encerramento: a thread principal aguarda o término das quatro threads, coleta as quatro somas parciais e calcula a soma total.

O arquivo inicial desse exercício está em `aula1/SomaSublistas.java`.

### 2. Filtro independente de dados (Map)

Contexto: limpeza e saneamento de bases de dados.

Problema: processar uma lista com 5.000 strings contendo nomes de usuários informados em um formulário.

Ação: dividir a lista em dois blocos. A primeira thread recebe a primeira metade e a segunda thread recebe a segunda metade. Cada uma processa sua sublista isoladamente, removendo espaços do início e do fim e convertendo o texto para letras maiúsculas.

Encerramento: cada thread produz uma nova lista limpa e a thread principal reúne os dois resultados.

## Contexto complementar do repositório do professor

O conteúdo do repositório do professor organiza a disciplina como uma progressão: fundamentos de sistemas distribuídos, threads, sockets, RPC/RMI, multicast e comunicação em grupo com JGroups.

### Processamento concomitante e paralelo

O material distingue o processamento concomitante, associado ao uso de threads, do processamento paralelo, associado a tecnologias como CUDA, OpenMP e MPI. Um grid é apresentado como exemplo de computação distribuída e concomitante, enquanto um cluster é apresentado como exemplo de computação paralela e fortemente acoplada.

Uma thread é tratada didaticamente como um pequeno processo dentro de um processo. Ela circunda uma rotina para que essa rotina possa executar concomitantemente com outras. Threads são indicadas para tarefas de tratamento e análise de dados e para liberar operações bloqueantes de comunicação. Elas não são necessariamente adequadas para tarefas muito pequenas ou para interação direta com o usuário.

### Threads sem memória compartilhada

Em uma solução sem memória compartilhada, cada thread recebe seus próprios parâmetros e trabalha sobre seus próprios dados. Não existe uma seção crítica acessada simultaneamente. No padrão Java mostrado pelo professor, uma classe estende `Thread`, recebe os parâmetros pelo construtor e implementa sua tarefa no método `run()`.

O fluxo principal cria as threads, chama `start()` e utiliza `join()` quando precisa aguardar a conclusão de uma etapa antes de iniciar a seguinte. As threads também podem ser identificadas por nome e ID durante a execução.

### Threads com memória compartilhada

Em uma solução com memória compartilhada, duas ou mais threads acessam o mesmo objeto ou a mesma estrutura de dados. Esse recurso comum forma uma seção crítica e pode sofrer uma condição de corrida, pois a ordem de execução das threads não é previsível.

No padrão Java estudado, a lista fica encapsulada em uma classe própria. As operações de escrita e leitura são marcadas com `synchronized`, permitindo que somente uma thread execute cada operação crítica por vez. O método de leitura devolve uma cópia da lista, evitando que código externo altere diretamente a estrutura protegida.

No segundo exercício da aula 2, `numeros1.txt` e `numeros2.txt` são lidos por threads diferentes. Ambas recebem pelo construtor a mesma instância de uma lista compartilhada e adicionam nela os números encontrados. A thread principal espera as duas leituras com `join()` e somente então exibe os 20 valores.

### Comunicação e sockets

A comunicação distribuída segue o modelo TCP/IP e envolve endereço IP, máscara de rede, porta lógica, socket e protocolo de transporte. Ela pode ocorrer como unicast, broadcast ou multicast. Operações de envio, escrita, recebimento e leitura são bloqueantes; por isso, threads ouvidoras e enviadoras são usadas para manter outras partes da aplicação em execução.

Na arquitetura cliente-servidor com TCP, o servidor utiliza `ServerSocket` para aguardar conexões e um `Socket` para representar cada cliente. O cliente utiliza um `Socket` com o endereço e a porta do servidor. Ambos usam objetos leitores e escritores para trocar dados.

O UDP não estabelece uma conexão como o TCP e é usado, por exemplo, em multicast. No multicast, os participantes usam um endereço de grupo, normalmente na faixa convencionada `239.x.x.x`, e uma porta. Cada estação pode atuar como remetente e destinatária, aproximando-se da arquitetura ponto a ponto.

### RPC e RMI

RPC permite que uma máquina solicite a execução de um procedimento em outra máquina. O cliente apresenta os serviços disponíveis e o servidor implementa esses serviços por meio de um contrato. Em Java, o mecanismo correspondente estudado é o RMI; o material também menciona RPC em Python e XML-RPC em C#.

### Comunicação em grupo

Depois de multicast, o conteúdo apresenta JGroups para comunicação entre membros de um grupo. Entre os problemas tratados estão a identificação dos participantes, notificações quando membros entram ou saem e atualização de mensagens para participantes que ingressam posteriormente.

### Sincronização distribuída

Além do `synchronized` usado entre threads de uma mesma JVM, a disciplina aborda sincronização entre máquinas. Os tópicos incluem relógios físicos, relógios lógicos de Lamport, exclusão mútua e eleição. Esses mecanismos auxiliam a ordenar eventos e coordenar o acesso a recursos quando não há um relógio global perfeito.
