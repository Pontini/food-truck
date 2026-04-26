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