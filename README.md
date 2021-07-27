# Documentação da linguagem
A documentação da linguagem está dividida em dois documentos:
- [A Definição geral da linguagem](https://docs.google.com/document/d/1qhGHx6IZtRRVakPwU8Of2hOtiNsTZCI-LYCrcxf6P68/edit?usp=sharing), a qual envolve:
  - Características gerais da linguagem
  - Tipos de dados e operadores
  - Precedência de operadores
  - Instruções (condicionais e iterativas) da linguagem
- [A Definição do compilador](https://docs.google.com/document/d/1dWhyP3m9N_mfsRLpniW5F5olvgvCqLDhDrFryV40hok/edit?usp=sharing), a qual envolve:
  - Linguagem de implementação
  - Enumeração das categorias dos tokens
  - Especificação dos tokens da linguagem
    - esses tokens são utilizados nas gramáticas em `GLC.txt` e `Gramática LL1.txt` como terminais

# Gramáticas
Inclusas no projeto estão duas gramáticas equivalentes, sendo uma GLC (Gramática Livre de Contexto) e uma Gramática LL1. Sendo a GLC apenas uma versão mais simplificada e de melhor legibilidade do que a gramática LL1. 

Por conta do tipo de compilador escolhido (preditivo recursivo), a gramática LL1 foi a utilizada para desenvolvimento, visto que o compilador analisa da esquerda para a direita e tem por objetivo saber que derivação escolher a partir do primeiro token observado para cada não-terminal. Dessa forma, duas derivações de um mesmo não-terminal não podem gerar um mesmo first (terminal na primeira posição). 

## Terminais e não-terminais
Nas gramáticas "terminais" são os termos que estão entre aspas simples, esses termos são os literais da linguagem, ou seja, eles são o código da linguagem em sí. Além disso, terminais podem se tratar de tokens definidos na documentação do compilador, por exemplo, o terminal 'funDef' representa `fun` na linguagem em sí e o terminal 'digit' representa um digito decimal (0-9) na linguagem em sí.

Obs: & significa vazio, esse simbolo foi usado no lugar de epsilon para simplicidade em questão de encoding de caracteres das gramáticas

Já os "não-terminais" são os termos que começam por letra maiúscula e não tem aspas simples em volta, esses são termos que tem derivações, ou seja, que aparecem à esquerda da igualdade de pelo menos uma linha da gramática. Dessa forma, seu valor de fato apenas é decidido ao ler os tokens subsequentes de código.
