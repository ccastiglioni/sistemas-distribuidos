# Exercícios — Sistemas Distribuídos

## 3. Relógios Físicos e Lógicos, Exclusão Mútua e Eleição

Pesquisar, compilar e disponibilizar nos GitHub pessoais sobre:

- Relógios Físicos
- Relógios Lógicos
- Exclusão Mútua
- Eleição

---

## 4. Pool de Threads

Pesquisar e compilar sobre a teoria de Pool de Threads.

Criar exemplos práticos utilizando listas.

---

## 5. Analisador de Logs Distribuído (MapReduce Local)

Criar um sistema para processar arquivos de log gigantescos.

Arquivo de log:

https://raw.githubusercontent.com/alexandrezamberlan/sistemasDistribuidos/refs/heads/master/00-exercicios_trabalhos/erro.log

### Cenário

Um processo coordenador lê o arquivo grande e divide o arquivo em pedaços menores.

### Regra do Pool

O coordenador envia cada pedaço para um pool de trabalhadores utilizando filas de mensagens (mensageria).

### Sem Memória Compartilhada

Os trabalhadores não podem alterar variáveis globais.

Cada trabalhador deve processar seu pedaço de forma isolada e devolver um resumo parcial, como a contagem de erros.

### Resultado

O coordenador junta todos os resumos parciais e apresenta o resultado final.
