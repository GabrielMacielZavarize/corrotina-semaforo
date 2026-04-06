# Corrotinas e Semáforo em Kotlin

Este projeto é uma demonstração prática de como usar corrotinas em Kotlin para controlar fluxo assíncrono de forma simples e legível. Aqui, a ideia foi simular um semáforo com dois modos:

- `AUTOMATICO`: alterna entre vermelho, verde e amarelo com tempos definidos
- `AMARELO_PISCANTE`: liga e desliga a luz amarela em intervalo curto

## Apresentação

Link da apresentação do grupo:

https://gamma.app/docs/Corrotinas-em-Kotlin-gks17kdmiakx770?mode=doc

## O que é corrotina?

Corrotina é uma forma de executar tarefas assíncronas sem bloquear a thread principal da aplicação. Em Kotlin, ela facilita operações como:

- esperar tempo com `delay`
- fazer chamadas de rede
- buscar dados no banco
- processar eventos em segundo plano
- coordenar várias tarefas ao mesmo tempo

Na prática, ela permite escrever código assíncrono com cara de código sequencial, deixando a leitura e a manutenção bem mais simples do que abordagens mais antigas com callbacks.

## Por que isso importa?

Em apps Android, bloquear a thread principal significa travar a interface. Corrotinas ajudam a evitar isso porque permitem pausar uma tarefa sem congelar a tela.

No nosso exemplo, o semáforo troca de estado usando `delay(...)`. Isso quer dizer que o app "espera" o tempo necessário sem travar a interface do usuário.

## Como este projeto usa corrotinas

O projeto usa `LaunchedEffect` no Jetpack Compose para iniciar uma corrotina sempre que o modo do semáforo muda.

Resumo do fluxo:

- quando o modo está em `AUTOMATICO`, a corrotina entra em loop e alterna entre vermelho, verde e amarelo
- quando o modo vai para `AMARELO_PISCANTE`, a corrotina anterior é cancelada e uma nova começa
- o estado da luz é atualizado com `mutableStateOf`, fazendo a UI reagir automaticamente

Isso é interessante porque mostra três ideias importantes ao mesmo tempo:

- execução assíncrona
- cancelamento automático de corrotinas
- atualização reativa da interface

## Exemplo prático do semáforo

No app, a lógica segue mais ou menos esta ideia:

```kotlin
LaunchedEffect(modoAtual) {
    when (modoAtual) {
        ModoSemaforo.AUTOMATICO -> {
            while (true) {
                luzAtual = LuzSemaforo.VERMELHO
                delay(3_000)

                luzAtual = LuzSemaforo.VERDE
                delay(3_000)

                luzAtual = LuzSemaforo.AMARELO
                delay(1_500)
            }
        }

        ModoSemaforo.AMARELO_PISCANTE -> {
            while (true) {
                luzAtual = LuzSemaforo.AMARELO
                delay(500)

                luzAtual = LuzSemaforo.APAGADO
                delay(500)
            }
        }
    }
}
```

Esse trecho mostra bem a principal vantagem da corrotina: o código fica direto, fácil de entender e sem necessidade de encadear callbacks.

## Como empresas grandes utilizam corrotinas

Empresas grandes normalmente usam programação assíncrona para lidar com alto volume de dados, interface responsiva e integração com serviços externos. No ecossistema Kotlin e Android, corrotinas entram justamente nesse tipo de cenário.

### Google

O próprio ecossistema Android incentiva fortemente o uso de corrotinas com Jetpack, ViewModel, Room e Retrofit. Em projetos grandes, isso ajuda a organizar chamadas de API, persistência local e eventos de interface sem bloquear a UI.

### Netflix

Em plataformas de streaming, tarefas assíncronas são essenciais para carregar catálogos, recomendações, sessões do usuário, telemetria e atualizações em tempo real. Em times que usam Kotlin, corrotinas fazem sentido para coordenar essas operações com menos complexidade.

### Uber

Apps de mobilidade dependem de atualização constante de localização, status de corrida, mapa, preços e comunicação com backend. Corrotinas são úteis para lidar com múltiplas tarefas concorrentes sem deixar a interface pesada.

### Marketplaces e fintechs

Empresas como grandes varejistas, bancos digitais e marketplaces costumam precisar de sincronização de pedidos, pagamentos, autenticação, notificações e consulta de dados em paralelo. Corrotinas ajudam a estruturar esse fluxo com mais clareza e controle.

## Vantagens das corrotinas

- código mais limpo do que callbacks aninhados
- melhor legibilidade
- cancelamento estruturado
- integração forte com Android moderno
- menor chance de travar a interface
- melhor organização de tarefas concorrentes

## Possíveis dúvidas

### 1. Corrotina é a mesma coisa que thread?

Não. Thread é um recurso do sistema operacional, mais pesado. Corrotina é mais leve e pode ser suspensa sem bloquear a thread.

### 2. `delay()` bloqueia a aplicação?

Não. `delay()` apenas suspende a corrotina por um tempo. A thread continua livre para outras tarefas.

### 3. Por que usar corrotina em vez de `Thread.sleep()`?

Porque `Thread.sleep()` bloqueia a thread atual. Já `delay()` não bloqueia, o que é muito mais seguro para Android.

### 4. O que acontece quando o modo do semáforo muda?

Como o projeto usa `LaunchedEffect(modoAtual)`, a corrotina anterior é cancelada e outra é iniciada com a nova lógica.

### 5. Corrotina serve só para animação ou temporizador?

Não. Ela é muito usada para:

- requisições HTTP
- banco de dados
- autenticação
- upload e download
- filas de processamento
- sincronização local e remota

### 6. Dá para usar corrotina no backend também?

Sim. Kotlin também usa corrotinas em backend, microsserviços, filas e processamento concorrente, principalmente com frameworks como Ktor e Spring com suporte a suspensão.

### 7. Se eu criar várias corrotinas, o app vai ficar pesado?

Não necessariamente. Corrotinas são leves. O problema normalmente não é a quantidade em si, mas sim criar tarefas sem controle, sem escopo ou sem cancelamento adequado.

### 8. Qual é a maior vantagem para quem está começando?

A maior vantagem é conseguir escrever fluxo assíncrono de um jeito mais natural. Você lê o código quase como uma sequência comum, mas sem perder desempenho e responsividade.

## Conclusão

Corrotinas são uma das ferramentas mais importantes do Kotlin moderno. Elas ajudam a construir aplicações mais organizadas, responsivas e fáceis de manter.

Neste projeto, o semáforo é um exemplo simples, mas suficiente para mostrar como uma corrotina pode controlar estados, esperar intervalos e reagir a mudanças sem travar a interface.
