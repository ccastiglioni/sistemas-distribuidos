Analisador de Logs Distribuído (MapReduce Local)

Os alunos devem criar um sistema para processar arquivos de log gigantescos (https://raw.githubusercontent.com/alexandrezamberlan/sistemasDistribuidos/refs/heads/master/00-exercicios_trabalhos/erro.log).

O Cenário: Um processo coordenador lê o arquivo grande. Ele divide o arquivo em pedaços menores.

A Regra do Pool: O coordenador envia cada pedaço para um pool de trabalhadores usando filas de mensagens (mensageria).

Sem Memória Compartilhada: Os trabalhadores não podem alterar variáveis globais. Cada trabalhador processa seu pedaço de forma isolada e devolve um resumo parcial (contagem de erros, por exemplo).

O Resultado: O coordenador junta todos os resumos e mostra o resultado final.

