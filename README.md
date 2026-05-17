# Simulador de Algoritmos de Substituição de Páginas

Trabalho da disciplina de **Sistemas Operacionais** — Universidade de Fortaleza (Unifor), Curso de Ciência da Computação.

Simulador escrito em **Java** que compara o desempenho de quatro algoritmos clássicos de substituição de páginas em memória virtual, contando o número de **faltas de página** (page faults) gerado por cada um sobre uma mesma cadeia de referências.

## Algoritmos implementados

| Método | Algoritmo | Estratégia |
|:-:|---|---|
| 1 | **FIFO** (First-In, First-Out) | Substitui a página que está há mais tempo na memória. |
| 2 | **LRU** (Least Recently Used) | Substitui a página menos recentemente usada. |
| 3 | **Relógio** (Clock / Segunda Chance) | Lista circular com bit de referência — dá uma "segunda chance" antes de substituir. |
| 4 | **Ótimo** (OPT / Belady) | Substitui a página que será usada mais distante no futuro (referência teórica). |

## Estrutura do projeto

```
PageReplacementSimulator/
├── src/main/java/com/unifor/simulator/
│   ├── Main.java                       # ponto de entrada (console / GUI)
│   ├── core/
│   │   ├── PageReplacementAlgorithm.java
│   │   ├── SimulationResult.java
│   │   └── Simulator.java
│   ├── algorithms/
│   │   ├── FIFO.java
│   │   ├── LRU.java
│   │   ├── Clock.java
│   │   └── Optimal.java
│   └── gui/
│       ├── SimulatorFrame.java         # interface Swing
│       └── ChartPanel.java             # gráfico de barras (Java2D)
├── build.sh / build.bat
└── README.md
```

## Pré-requisitos

- **JDK 11+** (testado com OpenJDK 17)
- Não há dependências externas — apenas Java padrão (Swing + Java2D)

Verifique com:
```bash
java -version
javac -version
```

## Como compilar

### Por que é preciso "buildar"?

Java é uma linguagem **compilada para bytecode**: o código-fonte (`.java`) **não roda direto**. Antes da execução, o compilador (`javac`) converte cada arquivo `.java` em um `.class` (bytecode), que é o formato que a JVM (`java`) sabe executar:

```
.java  ──[javac]──►  .class  ──[java]──►  programa rodando
(fonte)             (bytecode)             (JVM executa)
```

Os scripts [build.sh](build.sh) / [build.bat](build.bat) automatizam essa etapa: encontram todos os `.java` em `src/main/java/` e geram os `.class` correspondentes em `bin/`.

### Quando rodar o build?

| Situação | Precisa buildar? |
|---|---|
| **Primeira vez** que vai executar o projeto após clonar/baixar | ✅ Sim — ainda não existe a pasta `bin/` |
| Modificou **qualquer arquivo `.java`** | ✅ Sim — senão a JVM executa a versão antiga compilada |
| Apenas vai **executar de novo**, sem mudar o código | ❌ Não — os `.class` em `bin/` já estão prontos |
| Está usando uma **IDE** (IntelliJ, VS Code, Eclipse) | ❌ Não — a IDE recompila automaticamente ao apertar ▶️ Run |

> ⚠️ **Erro comum:** alterar o código `.java` e executar `java -cp bin ...` sem buildar antes. O programa vai rodar normalmente, mas com a **versão antiga** do código — porque a JVM lê o `.class` (resultado da última compilação), não o `.java` que você acabou de editar.

### Comandos

**Linux / macOS / Git Bash:**
```bash
chmod +x build.sh   # só na primeira vez
./build.sh
```

**Windows (PowerShell):**
```powershell
.\build.bat
```

**Windows (cmd):**
```bat
build.bat
```

**Manualmente (Linux/macOS):**
```bash
mkdir -p bin
javac -d bin $(find src/main/java -name "*.java")
```

**Manualmente (PowerShell):**
```powershell
New-Item -ItemType Directory -Force bin | Out-Null
javac -d bin (Get-ChildItem -Recurse src\main\java\*.java).FullName
```

Os `.class` serão gerados em `bin/`.

## Como executar

### Modo console (interativo)
```bash
java -cp bin com.unifor.simulator.Main
```

O programa pede:
1. **Cadeia de referência** — números separados por espaço/vírgula. ENTER para gerar aleatoriamente.
2. **Número de molduras (frames)** — padrão `3`.
3. **Passo a passo** — `s` mostra o estado das molduras a cada referência.

Saída: número de faltas de página por algoritmo.

### Modo gráfico (Swing — opcional, +1 ponto)
```bash
java -cp bin com.unifor.simulator.Main --gui
```

A interface gráfica permite:
- Editar a cadeia de referência manualmente ou gerar aleatoriamente.
- Ajustar número de molduras.
- Ver gráfico de barras comparativo das faltas de página.
- Inspecionar o passo a passo de cada algoritmo em abas.

## Exemplo de uso (cadeia clássica do Tanenbaum/Silberschatz)

Entrada:
```
Cadeia: 7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1
Molduras: 3
```

Saída:
```
=========================================================
 RESULTADOS - Faltas de pagina por algoritmo
=========================================================
Metodo 1 (FIFO)    - 15 faltas de pagina (taxa = 75,00%)
Metodo 2 (LRU)     - 12 faltas de pagina (taxa = 60,00%)
Metodo 3 (Relogio) - 11 faltas de pagina (taxa = 55,00%)
Metodo 4 (Otimo)   -  9 faltas de pagina (taxa = 45,00%)
=========================================================
```

Os valores de FIFO (15), LRU (12) e Ótimo (9) coincidem com os exemplos canônicos da literatura, validando a corretude da implementação.

## Observações

- O **Ótimo** não é um algoritmo prático (exige conhecer o futuro), mas serve como **limite inferior teórico** para comparação.
- O **Relógio** é uma boa aproximação do LRU com custo de implementação muito menor (não precisa atualizar lista a cada acesso).
- A cadeia gerada aleatoriamente usa `System.currentTimeMillis()` como semente; cada execução produz uma cadeia diferente.

## Autores

- Angelo Rodrigues
- _(co-autor, se houver)_

## Licença

Projeto acadêmico — uso educacional.
