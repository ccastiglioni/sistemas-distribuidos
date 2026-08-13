# Exercícios de fixação

## Threads

1. Leitura e Exibição de Listas sem Memória Compartilhada

Contexto: Leitura e organização de dados armazenados em arquivos de texto.

Problema: O arquivo `numeros.txt` contém 10 números inteiros, um por linha, e o arquivo `nomes.txt` contém 15 nomes, um por linha. Os dados de cada arquivo devem ser lidos e armazenados em listas próprias.

Ação: Crie uma thread para ler o arquivo `numeros.txt` e popular uma lista de números inteiros. Crie outra thread para ler o arquivo `nomes.txt` e popular uma lista de strings. Cada thread deve trabalhar somente com sua própria lista, sem memória compartilhada. Avalie a necessidade de métodos distintos para a leitura dos números e dos nomes.

Encerramento: A thread principal aguarda o término das threads de leitura. Em seguida, crie threads para exibir separadamente a lista de números e a lista de nomes. A thread principal deve aguardar o término das threads de exibição antes de finalizar o programa.

2. Leitura Concorrente de Arquivos com Memória Compartilhada

Contexto: Consolidação concorrente de dados numéricos provenientes de arquivos diferentes.

Problema: O arquivo `numeros1.txt` contém 10 números inteiros, um por linha, e o arquivo `numeros2.txt` contém outros 10 números inteiros, também um por linha. Os números dos dois arquivos devem ser armazenados em uma mesma lista de inteiros.

Ação: Crie duas threads para realizar as operações de leitura e população. Uma thread deve ler o arquivo `numeros1.txt` e a outra deve ler o arquivo `numeros2.txt`. As duas threads devem adicionar os valores à mesma lista de inteiros, utilizando memória compartilhada e os mecanismos de sincronização necessários para garantir a integridade dos dados.

Encerramento: A thread principal aguarda o término das duas threads de leitura e, depois, exibe a lista compartilhada contendo os 20 números carregados dos arquivos.
