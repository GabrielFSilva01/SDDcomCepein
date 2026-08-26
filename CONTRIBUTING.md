# Guia de Contribuição e Fork do Projeto

Agradecemos o seu interesse em contribuir para o **Sistema de Gestão de Produtos e Fornecedores (SDDcomCepein)**!

---

## ⚡ Como Fazer Fork e Rodar em 1 Clique

### 1. Fazer o Fork no GitHub
1. Acesse o repositório principal: [https://github.com/GabrielFSilva01/SDDcomCepein](https://github.com/GabrielFSilva01/SDDcomCepein).
2. No canto superior direito da página, clique no botão **Fork**.
3. Escolha a sua conta ou organização para criar a cópia do repositório.

### 2. Clonar o seu Fork Localmente
```bash
git clone https://github.com/SEU_USUARIO/SDDcomCepein.git
cd SDDcomCepein
```

### 3. Execução Automática em 1 Clique
- **Windows**: Dê 2 cliques no arquivo `start.bat` ou execute `.\start.bat` no terminal.
- **Linux / macOS**: Execute `bash start.sh` no terminal.

O script cuidará de instalar as dependências do frontend e subir ambos os servidores (Backend na porta 8080 e Frontend na porta 5173).

---

## 🔑 Credenciais para Login

- **Usuário**: `admin`
- **Senha**: `admin123`

---

## 🌿 Como Enviar Contribuições (Pull Requests)

1. Crie uma nova branch para a sua funcionalidade ou correção:
   ```bash
   git checkout -b minha-nova-feature
   ```
2. Realize as alterações mantendo os padrões de código e a **Constituição do Projeto** (`.specify/memory/constitution.md`).
3. Adicione e commite suas alterações com mensagens claras e descritivas em português:
   ```bash
   git commit -m "feat(modulo): minha nova melhoria"
   ```
4. Envie a branch para o seu repositório fork:
   ```bash
   git push origin minha-nova-feature
   ```
5. Abra um **Pull Request (PR)** para a branch `main` do repositório original.

---

## 📜 Diretrizes e Código de Conduta
- Mantenha a documentação no `README.md` sempre atualizada conforme exigido pelo Princípio VI da Constituição do projeto.
- Certifique-se de que os testes automatizados passem antes de enviar o PR (`mvn test` no backend).
