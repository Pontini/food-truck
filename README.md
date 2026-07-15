# 📌 Contexto

A Food Truck é uma empresa de delivery que opera por meio de aplicativos e conta com uma solução de atendimento ao cliente integrada às suas operações. Atualmente, essa solução é baseada na ferramenta Talkflow, que representa um custo anual de aproximadamente **R$ 40 milhões**.

Além do alto custo, a solução atual apresenta fragmentação entre plataformas, o que dificulta a padronização da experiência e a evolução do produto.

A Food Truck também possui uma **camada social**, que permite aos usuários interagir entre si por meio de mensagens (ex.: chat entre clientes, entregadores e estabelecimentos). Isso reforça ainda mais a relevância de uma solução de chat robusta, escalável e unificada dentro do ecossistema da empresa.

Este documento está organizado da seguinte forma: primeiro o **problema**, depois **onde queremos chegar** (as demandas), em seguida **como resolvo cada problema** do ponto de vista técnico, e por fim como a **qualidade** (testes e observabilidade) entra em todo esse racional.

---

# 🧩 O Problema

Resumindo o cenário atual em três frentes:

1. **Custo elevado**: a dependência da Talkflow custa ~R$ 40 milhões/ano, sem que a empresa tenha controle sobre essa infraestrutura.
2. **Fragmentação entre plataformas**: não existe uma solução única de chat — mobile, backend e web não seguem um padrão consistente, o que dificulta manutenção e evolução.
3. **Acoplamento ao monorepo**: o projeto está fortemente acoplado a classes internas, ferramentas proprietárias e dependências específicas do ecossistema da empresa, o que impede que a solução seja extraída como um produto independente.

Além disso, no futuro, a intenção é estender essa solução de chat também para a web — assim como acontece hoje com o WhatsApp, que roda tanto no mobile quanto no navegador (WhatsApp Web). Isso reforça ainda mais a necessidade de uma arquitetura pensada desde já para multiplataforma, e não apenas para o contexto mobile.

---

# 🎯 Onde Quero Chegar (Demandas)

A partir desses três problemas, defino três demandas principais:

## 1. Redução de Custos
Substituir gradualmente a dependência da Talkflow por uma solução própria, reduzindo significativamente o custo anual de operação.

## 2. Unificação da Plataforma de Chat
Criar uma solução única de chat que funcione de forma consistente em todas as plataformas (mobile, backend e web), atendendo tanto o suporte ao cliente quanto a camada social da aplicação.

## 3. Evolução para Produto de Tecnologia
Evoluir a solução interna de chat para um produto independente, permitindo que a Food Truck:
- deixe de ser apenas uma empresa de delivery;
- ofereça sua solução de chat como uma ferramenta própria de comunicação — nos moldes de um WhatsApp —, e não apenas como um serviço de atendimento nos moldes da Talkflow.

---

# 🛠️ Como Resolvo Cada Problema

## Resolvendo o Custo: migração gradual, sem "big bang"

**Ideia geral:** em vez de trocar a Talkflow pela solução própria de uma vez só — o que seria arriscado —, migro a base de usuários aos poucos, validando que a solução própria funciona bem antes de expandir, e mantendo a possibilidade de voltar atrás rapidamente se algo der errado. É com base nas métricas de observabilidade que valido esse desempenho e tomo a decisão de aumentar o rollout com segurança.

**Detalhe técnico:** a estratégia é ter dois `DataSources` — um para o Talkflow e outro para a solução própria —, ficando a cargo do `Repository` orquestrar qual deles utilizar em cada momento. Essa escolha é controlada por **feature flag**: ligo a solução própria para uma fatia da base, acompanho de perto com observabilidade e, com confiança, vou expandindo o rollout até desligar completamente a dependência da Talkflow.

## Resolvendo a Fragmentação: uma única fonte de verdade para todas as plataformas

**Ideia geral:** o problema de fragmentação existe porque cada plataforma resolve o chat do seu próprio jeito. A solução é ter uma única lógica, escrita uma vez, que todas as plataformas consomem — garantindo consistência sem duplicar esforço.

**Detalhe técnico:** isso é resolvido pela modularização em `public`/`impl` (detalhada na seção de Arquitetura): os contratos ficam concentrados em módulos Kotlin puros, sem dependências de plataforma, permitindo que a mesma lógica seja reaproveitada em mobile, backend e web.

## Resolvendo o Acoplamento ao Monorepo: isolar o que é específico da empresa

**Ideia geral:** para que a solução possa um dia sair do monorepo e virar produto, tudo que é específico da empresa (ferramentas internas, dependências proprietárias) precisa estar isolado em um único lugar — de forma que o restante do código não saiba, nem precise saber, que essas dependências existem.

**Detalhe técnico:** isso é resolvido isolando as dependências específicas da empresa nos módulos `implAndroid`/`impl`. Com isso, os contratos (`public`) ficam livres para serem extraídos e reutilizados fora do ecossistema atual, tornando viável transformar a solução em um produto independente e comercializável.

## Resolvendo o Uso do Código na Web

**Ideia geral:** o objetivo é reaproveitar o código que já foi escrito, em vez de reescrever tudo do zero para a web. A estratégia é evoluir em duas etapas: primeiro uma solução simples e rápida de implementar, e só depois — quando fizer sentido — migrar para uma abordagem mais robusta e definitiva.

**Detalhe técnico:** hoje o projeto é nativo, com apps separados para iOS e Android — ou seja, não existe nenhum código compartilhado entre as plataformas. Para resolver a web sem duplicar lógica, o caminho é gerar esse *shared code* a partir do módulo `impl`, que já é escrito em Kotlin puro no Android.

Em um primeiro momento, a solução mais "óbvia" seria adotar KMP (Kotlin Multiplatform). Porém, pensando em simplicidade e no esforço de curto prazo, gerar um `.jar` a partir do módulo `impl` já seria suficiente para atender ao projeto nesse momento — sem exigir mudanças bruscas na arquitetura atual nem envolver o iOS nesse primeiro passo. Essa abordagem também é mais alinhada ao dia a dia do time: refatorações menores são mais fáceis de planejar, alocar e executar de forma incremental, em vez de exigir um esforço concentrado de migração para KMP logo de início.

Em um segundo momento, com o `implAndroid` já isolando toda a camada de framework Android, o caminho para adotar KMP fica muito mais natural — já que as dependências específicas de plataforma já estariam devidamente isoladas. Nesse ponto, esse mesmo *shared code*, que hoje nasceria a partir do Android, passaria a ser reaproveitado também pelo iOS (que hoje é nativo e não compartilha nada com o Android), padronizando web, Android e iOS em uma única base de código, sem o risco de uma migração precipitada.

---

# 🚀 Direção Estratégica

O sucesso deste projeto permitirá que a Food Truck:
- reduza custos operacionais;
- unifique a comunicação entre usuários e suporte em uma única plataforma;
- ganhe controle sobre sua própria infraestrutura de comunicação;
- potencialize sua camada social dentro do aplicativo;
- crie uma nova linha de negócio baseada em tecnologia.

---

# 🧱 Arquitetura Proposta

## Módulos

### `public`
Contratos puramente Kotlin, que devem ser implementados pelos módulos `impl` ou `implAndroid`.

### `publicAndroid`
Contratos específicos de Android. Este módulo pode abstrair:
- frameworks Android;
- bibliotecas de terceiros;
- classes internas do projeto.

### `implAndroid`
Implementações concretas para Android, isolando dependências de:
- bibliotecas de terceiros;
- classes internas do monorepo.

### `impl`
Implementação concreta sem dependências de Android, possibilitando o uso multiplataforma (iOS, web, etc.).

## 💡 Módulo de Observabilidade

Este módulo simula um caso real: uma solução interna da empresa que **não foi bem modularizada**.

Como o objetivo do projeto é criar uma solução unificada entre plataformas e agnóstica em relação à empresa — de forma a viabilizar sua comercialização como produto de chat —, foi necessário criar uma *facade* e isolar essa dependência dentro do `implAndroid`. Este é, portanto, um exemplo prático de como o `implAndroid` deve ser utilizado.

## 💡 Objetivo da Modularização

Essa divisão permite:
- isolar dependências específicas de Android;
- evitar acoplamento com o monorepo;
- facilitar a extração do projeto como produto independente;
- viabilizar o suporte multiplataforma.

No final, todas as dependências específicas do projeto — Android, frameworks, etc. — ficam isoladas no `implAndroid`, permitindo decidir com clareza como tratá-las.

---

# ⚙️ Responsabilidades

### ViewModel
Responsável por orquestrar a lógica de apresentação, interagindo com os casos de uso e expondo estados para a UI.

### ChatManager
Responsável por gerenciar o estado da conexão (REST/WebSocket) e o ciclo de vida.

> **Observação:** neste projeto foram utilizadas APIs fictícias. Em um cenário real, o ChatManager seria responsável por manter conexões ativas (ex.: WebSocket) e atualizar a UI em tempo real com novas mensagens.

### Intents
Utilizados para controlar as entradas da UI, tornando o fluxo de dados mais previsível e organizado.

### States
Centralizam o estado da tela, facilitando o debug e o entendimento do fluxo.

### Repositories
Orquestram a comunicação com APIs (REST/WebSocket) e fornecem dados para a camada de UI.

### DataSources
Implementam a comunicação direta com as APIs (REST/WebSocket).

---

# ⚙️ Estratégia de Cache

**Tela de contatos / últimas mensagens:** a ideia é exibir a tela de chat com cache local e, assim que os dados são baixados, atualizar a tela.

Para a listagem de mensagens, a busca é feita via HTTP. Uma evolução futura seria buscar via WebSocket, que é mais performático e tem menor latência.

**Tela de chat:** aqui foi usada uma abordagem mais criativa, já que o backend não foi codificado e o projeto utiliza APIs fictícias. As mensagens são buscadas no banco de dados local e, conforme o usuário envia mensagens e recebe as respostas, o banco de dados local é atualizado.

---

# 🧠 Considerações Arquiteturais

- Qualquer decisão pode ser vista como overengineering ou como algo totalmente necessário — tudo depende do contexto.

- Padrões de projeto devem ser **adaptados**, não seguidos rigidamente. O foco deve ser resolver o problema da melhor forma possível dentro do contexto.

- O uso de *use cases* apenas como repasse direto de chamadas pode ser considerado overengineering. O mais importante é manter responsabilidades bem definidas e uma arquitetura clara.

- Tornar um projeto totalmente agnóstico de frameworks pode levar à reinvenção da roda. É essencial ter critério na escolha e no isolamento de dependências.

  Exemplos de reflexão:
  - Faz sentido isolar o Koin?
  - Faz sentido isolar o Retrofit?
  - Faz sentido isolar um provider de chat?

  A resposta, novamente, depende do contexto.

## 🧩 Mappers: Construtor vs. Extension Function

Prefiro utilizar *mappers* via construtor, mas já utilizei também como *extension functions*. Ambas as abordagens têm prós e contras:

**Via construtor**
- Facilita a injeção de dependências.
- Torna o fluxo mais explícito.
- Pode aumentar a complexidade e o número de classes.

**Via extension function**
- Deixa o código mais conciso e direto.
- Reduz boilerplate.
- Pode dificultar testes e a evolução quando há dependências envolvidas.

No fim, a escolha depende do contexto — especialmente da complexidade da transformação (ex.: encadeamento de parses) — e também da preferência da equipe.

## 🧠 Camadas e Modelos de Presentation

Existe bastante discussão sobre a criação de uma camada de *models* para a presentation, com o objetivo de evitar o acesso direto ao domain.

O cuidado aqui é não cair no anti-pattern de criar camadas que não agregam valor — ou seja, estruturas que apenas repassam dados sem transformação ou regra de negócio.

### 💡 Exemplo Prático

Em um projeto onde precisei implementar um SDUI (Server-Driven UI):

- Em vez de criar uma camada de *domain models*,
- optei por eliminar essa camada
- e mapear diretamente os dados da API para componentes de UI (camada de presentation).

**Isso está errado?**
- Pela literatura clássica: sim.
- Na prática: funcionou bem.

Com essa abordagem, conseguimos:
- reduzir a complexidade;
- diminuir código desnecessário;
- acelerar a entrega.

Até hoje, não sentimos falta da camada de domain nesse contexto.

O ponto principal é: se um dia for necessário introduzir essa camada, é totalmente possível evoluir o design sem grandes problemas.

Começar simples e evoluir conforme a necessidade tende a ser mais eficiente do que antecipar complexidade. Criar abstrações só faz sentido quando elas resolvem um problema real — não apenas para seguir um padrão.

## ⚡ Contexto, Mercado e Pragmatismo

Os tempos mudaram.

Lembro como se fosse ontem o quanto era difícil conseguir um cartão de crédito — muitas vezes, era preciso praticamente implorar por aprovação. Hoje, até pessoas com score baixo conseguem acesso com relativa facilidade. Isso evidencia o quanto o mercado se tornou mais agressivo e competitivo.

Esse cenário impacta diretamente a forma como construímos tecnologia: existe uma pressão real por **entregar valor rapidamente**.

Isso não significa abrir mão de boas práticas — pelo contrário, elas continuam sendo fundamentais. Mas é importante lembrar: **a tecnologia existe para nos servir, não o contrário.**

O foco deve ser sempre:
- resolver o problema da melhor forma possível;
- considerando o contexto;
- equilibrando qualidade, prazo e complexidade.

Boas decisões técnicas não são apenas sobre seguir padrões, mas sobre fazer escolhas conscientes diante da realidade do projeto.

---

# ✅ Qualidade: Testes e Observabilidade

Além de resolver os problemas técnicos e de arquitetura, uma parte importante de como penso o projeto é garantir qualidade — e isso passa por duas frentes complementares: testes unitários e observabilidade.

## Testes Unitários

Quando existe lógica de negócio, os testes da camada de domain são os mais importantes — é ali que as regras críticas do sistema estão concentradas, e são elas que mais precisam de garantia de comportamento.

Quando não há uma lógica de negócio relevante, alguns pontos ainda merecem atenção especial:

- **Parse do JSON da API para os modelos**: um teste bastante interessante de se aplicar, pois evita problemas comuns de parsing — campos opcionais tratados incorretamente, mudanças de contrato passando despercebidas, tipos inesperados, entre outros.
- **Mappers**: é onde, muitas vezes, ocorre a conversão de dados entre camadas (API → domain, domain → presentation, etc.). Como esses mappers concentram regras de transformação, também merecem cobertura de testes.
- **Repository**: concentra lógicas de orquestração, como decidir qual DataSource utilizar (cache local, REST, WebSocket, ou até mesmo Talkflow vs. solução própria) em cada cenário. Por envolver esse tipo de decisão, também é um bom candidato a testes.
- **ViewModel**: por lidar diretamente com o estado das telas, testar o ViewModel garante que as transições de estado (loading, sucesso, erro) aconteçam como esperado diante de cada cenário.

## Contratos de API e Tratamento de Erros

Um erro muito comum é não respeitar os contratos definidos pela API, criando classes com campos opcionais e valores default para "contornar" possíveis inconsistências.

À primeira vista, isso pode parecer uma solução razoável para evitar que o app crashe. Na prática, porém, é um problema: valores default podem mascarar falhas reais e passar despercebidos, gerando um comportamento silenciosamente incorreto.

Um exemplo claro: imagine um app bancário ou uma wallet em que, por algum problema de parse, o saldo do cliente venha com valor `0`. O cliente vai se assustar, achando que o dinheiro sumiu — quando, na verdade, o problema é técnico, não financeiro.

Por isso, o mobile deve sempre respeitar os contratos definidos:

- Não deve crashar o app quando o contrato não for respeitado.
- Deve exibir uma tela de erro genérica (algo como "algo inesperado aconteceu"), em vez de mascarar o problema com um valor default.
- Deve usar observabilidade para notificar a equipe de que algo está errado, permitindo identificar e corrigir a causa raiz rapidamente.

Essa combinação — respeitar o contrato, falhar de forma visível e observável — evita que problemas técnicos se transformem em problemas de confiança do usuário.

## Observabilidade e Monitoramento

Não existe argumento melhor do que os números.

Imagine chegar em uma call de alinhamento e priorização e, como referência técnica, afirmar que a conexão via WebSocket "não está boa" e que precisa ser refatorada — sem nenhum dado que sustente essa afirmação. Dificilmente esse argumento vai convencer alguém ou virar prioridade.

Agora imagine chegar na mesma call apontando que a taxa de conexão via WebSocket está caindo, ou que a taxa de falha de conexão está acima do aceitável. A conversa muda completamente — o que antes era uma opinião passa a ser um fato mensurável, e a decisão de priorizar (ou não) a refatoração deixa de depender de percepção e passa a depender de dados.

É exatamente por isso que a observabilidade é importante: ela é o que transforma percepção técnica em argumento de negócio.

Além disso, a observabilidade ajuda a antecipar o caos — com alertas de anomalia, acompanhamento de taxa de sucesso e monitoramento de crashes, é possível identificar um problema antes que ele vire um incidente maior, em vez de descobri-lo apenas quando o usuário já foi impactado. É também essa mesma observabilidade que sustenta a migração gradual via feature flag: sem dados em tempo real, não teria como saber se a solução própria está performando tão bem (ou melhor) que a Talkflow antes de expandir o rollout.

### Conexão WebSocket
Métricas que medem a saúde da conexão em tempo real — essenciais para identificar instabilidade antes que ela vire reclamação de usuário.

- **Success rate**: percentual de conexões estabelecidas com sucesso em relação ao total de tentativas.
- **Erro por minuto**: volume de erros de conexão ao longo do tempo, útil para identificar picos e correlacionar com deploys ou instabilidades externas.
- **Instabilidade da conexão WS**: frequência de quedas e reconexões, indicando o quão "confiável" a conexão está se mantendo ao longo da sessão do usuário.
- **Taxa de sucesso de conexão**: proporção de conexões que se mantêm estáveis, sem quedas, dentro de uma janela de tempo.
- **Taxa de falha**: proporção de tentativas de conexão que falham, seja por timeout, erro de rede ou rejeição do servidor.

**Eventos:**
- `ws_connection_success`: disparado quando a conexão WebSocket é estabelecida com sucesso.
- `ws_connection_failed`: disparado quando a tentativa de conexão falha.

### Envio de Mensagem
Métricas que medem a confiabilidade da entrega de mensagens — o core da experiência de chat.

- `ws_send_success`: mensagem enviada e confirmada com sucesso pelo servidor.
- `ws_send_failed`: falha no envio da mensagem, seja por perda de conexão ou erro do servidor.

### Logs de Engenharia
Logs mais granulares, voltados para debug e troubleshooting do ciclo de vida da conexão.

- `ws_connect_start`: início da tentativa de conexão.
- `ws_connected_success`: conexão estabelecida com sucesso.
- `ws_connection_error`: erro ocorrido durante a conexão.
- `ws_already_connected`: tentativa de conexão identificada quando já existe uma conexão ativa.
