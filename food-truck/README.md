# 📌 Contexto do Projeto

A Food Truck é uma empresa de delivery que opera por meio de aplicativos e possui uma solução de atendimento ao cliente integrada às suas operações. Atualmente, essa solução é baseada na ferramenta Talkflow, que representa um custo anual de aproximadamente **R$ 40 milhões**.

Além do alto custo, a solução atual apresenta fragmentação entre plataformas, dificultando a padronização da experiência e a evolução do produto.

Outro ponto importante é que a Food Truck também possui uma **camada social**, permitindo que usuários interajam entre si por meio de mensagens (ex: chat entre clientes, entregadores e estabelecimentos). Isso aumenta ainda mais a relevância de uma solução de chat robusta, escalável e unificada dentro do ecossistema da empresa.

---

# 🎯 Objetivos do Projeto

O projeto tem três objetivos principais:

## 1. Redução de Custos
Substituir gradualmente a dependência da Talkflow por uma solução própria, reduzindo significativamente o custo anual de operação.

## 2. Unificação da Plataforma de Chat
Criar uma solução única de chat que funcione de forma consistente em todas as plataformas (mobile, backend e web), atendendo tanto o suporte ao cliente quanto a camada social da aplicação.

## 3. Evolução para Produto de Tecnologia
Evoluir a solução interna de chat para um produto independente, permitindo que a Food Truck:
- deixe de ser apenas uma empresa de delivery
- passe a atuar também como uma empresa de tecnologia
- ofereça sua solução de chat como serviço para o mercado (modelo semelhante ao Talkflow)

---

# ⚠️ Desafio Técnico Atual

Atualmente, o projeto está inserido em um **monorepo**, fortemente acoplado a:
- classes internas
- ferramentas proprietárias
- dependências específicas do ecossistema da empresa

Isso dificulta a extração da solução como um produto independente, exigindo:
- desacoplamento da arquitetura
- definição de contratos claros (APIs/SDKs)
- modularização da solução

---

# 🚀 Direção Estratégica

O sucesso deste projeto permitirá que a Food Truck:
- reduza custos operacionais
- unifique a comunicação entre usuários e suporte em uma única plataforma
- ganhe controle sobre sua própria infraestrutura de comunicação
- potencialize sua camada social dentro do aplicativo
- crie uma nova linha de negócio baseada em tecnologia

---

# 🧱 Arquitetura Proposta

## Módulos

### `public`
Contratos puramente Kotlin que devem ser implementados pelos módulos `impl` ou `implAndroid`.

### `publicAndroid`
Contratos específicos de Android. Este módulo pode abstrair:
- frameworks Android
- bibliotecas de terceiros
- classes internas do projeto

### `implAndroid`
Implementações concretas para Android, isolando dependências de:
- bibliotecas de terceiros
- classes internas do monorepo

### `impl`
Implementação concreta sem dependências de Android, possibilitando uso multiplataforma (iOS, web, etc).

---

## 💡 Modulo de observabilidade 
Este modulo é pra simular um modulo que nao foi bem modularizado, e que é uma solucao interna da empresa.
Como o objetivo é tornar uma solucao unificada entre as plataformas e ter uma solucao agunostica da empresa para poder se vender como empresa de chat,
fez se necessário criar um facade e isolar no implAndroid. Essa seria exemplo pratico do implAndorid


## 💡 Objetivo da Modularização

Essa divisão permite:
- isolar dependências específicas de Android
- evitar acoplamento com o monorepo
- facilitar a extração do projeto como produto independente
- viabilizar multiplataforma
- No final, teremos as depndencias do projeto, android, frameworks isolados no implAndroid, sendo possível tomar decisao de como resolver.

---

# ⚙️ Responsabilidades

### ViewModel
Responsável por orquestrar a lógica de apresentação, interagindo com os casos de uso e expondo estados para a UI.

### ChatManager
Responsável por gerenciar o estado da conexão (REST/WebSocket) e o ciclo de vida.

> Observação: neste projeto foram utilizadas APIs fictícias. Em um cenário real, o ChatManager seria responsável por manter conexões ativas (ex: WebSocket) e atualizar a UI em tempo real com novas mensagens.

### Intents
Utilização de intents para controlar entradas da UI, tornando o fluxo de dados mais previsível e organizado.

### States
Centralização do estado da tela, facilitando debug e entendimento.

### Repositories
Orquestram a comunicação com APIs (REST/WebSocket) e fornecem dados para a camada de UI.

### DataSources
Implementam a comunicação direta com APIs (REST/WebSocket).

# ⚙️ Estrategia de cache:
Tela de contatos/ultimas mensagens: A ideia é mostrar a tela de chat com cache local, e assim que baixar os dados atualizar a tela.
Para a listagem de mensagens buscamos via HTTP. Mas uma evolucao futura seria buscar via WebSocket que é mais performático e tem menor latência.

Tela de chat: Aqui eu usei um pouco a criatividade pq como eu nao codifiquei backend e estou usando APIs fictícias, eu acabo buscando as mensagens no banco de dados local, e conforme eu envio mensagem e recebo o retorno 
eu vou atualiando banco de dados local. 



# 🧠 Considerações Arquiteturais

- Tudo pode ser considerado overengineering — ou totalmente necessário. Tudo depende do contexto.

- Padrões de projeto devem ser **adaptados**, não seguidos rigidamente. O foco deve ser resolver o problema da melhor forma possível dentro do contexto.

- O uso de *use cases* apenas como repasse direto de chamadas pode ser considerado overengineering. O mais importante é manter responsabilidades bem definidas e uma arquitetura clara.

- Tornar um projeto totalmente agnóstico de frameworks pode levar à reinvenção da roda. É essencial ter critério na escolha e no isolamento de dependências.

  Exemplos de reflexão:
    - Faz sentido isolar o Koin?
    - Faz sentido isolar o Retrofit?
    - Faz sentido isolar um provider de chat ? 

  A resposta, novamente, depende do contexto.

## 🧩 Mappers: Construtor vs Extension Function

Gosto de utilizar *mappers* via construtor, mas já utilizei também como *extension functions*. Ambas abordagens têm seus prós e contras:

- **Via construtor**
    - Facilita a injeção de dependências
    - Torna o fluxo mais explícito
    - Pode aumentar a complexidade e o número de classes

- **Via extension function**
    - Deixa o código mais conciso e direto
    - Reduz boilerplate
    - Pode dificultar testes e a evolução quando há dependências envolvidas

No fim, a escolha depende do contexto — especialmente da complexidade da transformação (ex: encadeamento de parses) — e também da preferência da equipe.

---

## 🧠 Camadas e Modelos de Presentation

Existe bastante discussão sobre a criação de uma camada de *models* para a presentation, com o objetivo de evitar o acesso direto ao domain.

O cuidado aqui é não cair no anti-pattern de criar camadas que não agregam valor — ou seja, estruturas que apenas repassam dados sem transformação ou regra.

### 💡 Exemplo prático

Em um projeto onde precisei implementar um SDUI (Server-Driven UI):

- Em vez de criar uma camada de *domain models*
- Optei por eliminar essa camada
- E mapear diretamente os dados da API para componentes de UI (camada de presentation)

**Isso está errado?**

- Pela literatura clássica: sim
- Na prática: funcionou bem

Conseguimos:
- Reduzir complexidade
- Diminuir código desnecessário
- Acelerar a entrega

Até hoje, não sentimos falta da camada de domain nesse contexto.

E o ponto principal:  
Se um dia for necessário introduzir essa camada, é totalmente possível evoluir o design sem grandes problemas.


Começar simples e evoluir conforme a necessidade tende a ser mais eficiente do que antecipar complexidade.

Criar abstrações só faz sentido quando elas resolvem um problema real — não apenas para seguir um padrão.

## ⚡ Contexto, Mercado e Pragmatismo

Os tempos mudaram.

Lembro como se fosse ontem o quanto era difícil conseguir um cartão de crédito — muitas vezes, era preciso praticamente implorar por aprovação.

Hoje, até pessoas com score baixo conseguem acesso com relativa facilidade. Isso evidencia o quanto o mercado se tornou mais agressivo e competitivo.

Esse cenário impacta diretamente a forma como construímos tecnologia: existe uma pressão real por **entregar valor rapidamente**.

Isso não significa abrir mão de boas práticas. Pelo contrário — elas continuam sendo fundamentais.

Mas é importante lembrar:  
**a tecnologia existe para nos servir, não o contrário.**

O foco deve ser sempre:
- resolver o problema da melhor forma possível
- considerando o contexto
- equilibrando qualidade, prazo e complexidade

Boas decisões técnicas não são apenas sobre seguir padrões, mas sobre fazer escolhas conscientes diante da realidade do projeto.

## ⚡ Observabilidade e Monitoramento
- success rate
- erro por minuto
- instabilidade do WS
-  taxa de sucesso de conexão
- taxa de falha
* ws_connection_success
* ws_connection_failed

Envio de mensagem
* ws_send_success
* ws_send_failed


Logs de ENGENHARIA 

ws_connect_start
ws_connected_success
ws_connection_error
ws_already_connected