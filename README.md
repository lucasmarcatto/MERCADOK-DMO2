# Micro Rede Social - Marketplace (MERCADOK)

Projeto desenvolvido para a disciplina **Dispositivos Móveis 2 (ARQDM02)**, ministrada pelo professor **Henrique Galati** no **IFSP - Campus Araraquara**, curso de **Análise e Desenvolvimento de Sistemas** (5º Semestre).

O aplicativo é uma micro rede social no estilo marketplace, onde os usuários podem criar conta, compartilhar produtos com fotos e localização, visualizar um feed de postagens e buscar por cidade.

---

## 🎯 Requisitos Atendidos

### 1. Autenticação e Cadastro de Usuário
- Tela de login com logotipo, campos de e‑mail e senha, botão de login e botão para criar novo usuário.
- Tela de cadastro com campos de nome completo, e‑mail, senha e confirmação de senha.
- Autenticação via **Firebase Authentication** com e‑mail e senha.
- Redirecionamento automático para a tela inicial (Home) se o usuário já estiver autenticado.

### 2. Postagens
- Criação de postagens contendo uma imagem (selecionada da galeria), um texto descritivo e a cidade (opcional – obtida automaticamente via GPS).
- As postagens são salvas no **Firebase Firestore** com todos os metadados (imagem codificada em Base64, descrição, cidade e autor).

### 3. Feed e Interações
- Feed carregado a partir do Firestore com paginação (5 postagens por vez, usando cursor baseado em timestamp).
- Busca de postagens pelo nome exato da cidade.
-  Tela de perfil do usuário: edição de nome completo, foto de perfil e alteração de senha (em tela separada).

### 4. Mapas e Localização
-  Ao criar uma postagem, o aplicativo obtém a localização atual do dispositivo (GPS) e traduz as coordenadas em nome da cidade usando **Geocoder** (caso disponível).

### Requisitos Não Funcionais
-  Desenvolvido em **Kotlin** (API 33 – Android 13 Tiramisu).
-  Utiliza **Firebase Authentication** e **Firebase Firestore**.
-  Telas responsivas e adaptáveis a diferentes tamanhos de tela.
-  Código escrito seguindo boas práticas aprendidas em aula (separação de responsabilidades).

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Kotlin
- **IDE:** Android Studio
- **Plataforma de nuvem:** Firebase
  - **Authentication:** cadastro e login de usuários
  - **Firestore Database:** armazenamento de usuários e postagens
- **Serviços Google Play:** Fused Location Provider (localização)
- **Componentes Android:** View Binding, RecyclerView, CardView, ActivityResult API (galeria e permissões)
- **Armazenamento de imagens:** As imagens são convertidas para Base64 e salvas diretamente no Firestore (estratégia de aula para evitar cobrança no Firebase Storage).

---

## 📁 Estrutura do Projeto

A organização dos pacotes segue o padrão apresentado nas aulas:

br.com.lucasmarcatto.microrslucasmarcartto  
├── adapter  
│ └── PostAdapter.kt  
├── auth  
│ └── AuthHelper.kt  
├── dao  
│ ├── UserDAO.kt  
│ └── PostDAO.kt  
├── location  
│ └── LocalizacaoHelper.kt  
├── model  
│ ├── User.kt  
│ └── Post.kt  
├── ui  
│ ├── LoginActivity.kt  
│ ├── RegisterActivity.kt  
│ ├── HomeActivity.kt  
│ ├── CreatePostActivity.kt  
│ ├── ProfileActivity.kt  
│ └── ChangePasswordActivity.kt  
└── util  
└── Base64Converter.kt


- `model`: classes de dados (`User`, `Post`).
- `dao`: acesso ao Firestore (salvar, buscar, atualizar).
- `auth`: centraliza operações do Firebase Authentication.
- `adapter`: adaptador do RecyclerView para o feed.
- `ui`: Activities responsáveis pela interface e interação com o usuário.
- `location`: helper para obtenção da localização e geocodificação.
- `util`: utilitário para conversão entre imagens e strings Base64.

---

## 🚀 Como Executar o Projeto

1. Clone este repositório:
   ```bash
   git clone https://github.com/lucasmarcatto/MERCADOK-DMO2.git
   ```
2. Abra o projeto no Android Studio (versão Hedgehog ou superior recomendada).
3. No Console do Firebase, crie um projeto e adicione um app Android com o pacote:
```
br.com.lucasmarcatto.microrslucasmarcartto
```
4. Baixe o arquivo `google-services.json` e coloque‑o na pasta `app/` do projeto.
5. No Firebase:
    - Ative o método de login **E-mail/senha** em **Authentication**.
    - Crie um banco de dados **Cloud Firestore** no modo de teste (ou configure as regras como preferir).
6. Sincronize o Gradle e execute o app em um emulador com API 33 ou superior.
---
## 🎥 Vídeos de Demonstração

### Demonstração curta (até 30 segundos)

https://github.com/user-attachments/assets/f5a3e849-2b49-448e-af5d-286c1aa495de

### Vídeo explicativo (5 a 10 minutos)

[https://youtu.be/9PxEWm9tbZM]  

---
## 👤 Autor

**Lucas Marcatto**  
Estudante de Análise e Desenvolvimento de Sistemas – IFSP Araraquara

---

> Este projeto foi desenvolvido exclusivamente para fins educacionais, seguindo as orientações da disciplina.
