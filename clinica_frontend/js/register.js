document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("login-form");
    const usernameInput = document.getElementById("username");
    const passwordInput = document.getElementById("password");
    const roleDropdown = document.getElementById("roles"); // ID corrigido
  
    form.addEventListener("submit", async (event) => {
      event.preventDefault();
  
      // Captura os valores do formulário
      const username = usernameInput.value.trim();
      const password = passwordInput.value.trim();
      const selectedRole = roleDropdown.value;
  
      // Mapeia o valor da dropdown para a role do backend
      const roleMapping = {
        student: "aluno",
        teatcher: "professor", // Corrigir typo: "teatcher" -> "teacher" no HTML se necessário
      };
      const role = roleMapping[selectedRole];
  
      // Valida os dados do formulário
      if (!username || !password || !role) {
        alert("Por favor, preencha todos os campos corretamente.");
        return;
      }
  
      // Cria o objeto do novo usuário
      const newUser = {
        username,
        password,
        role, // Envia a role traduzida
      };
  
      try {
        // Faz a requisição POST para o endpoint /users
        const response = await fetch("http://localhost:8080/users", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(newUser),
        });        
  
        // Trata a resposta do backend
        if (response.ok) {
          const createdUser = await response.json();
          alert(`Usuário criado com sucesso! ID: ${createdUser.id}`);
          // Limpa o formulário
          form.reset();
        } else if (response.status === 400) {
          alert("Erro: Dados inválidos. Verifique os campos.");
        } else {
          alert("Erro inesperado ao criar o usuário.");
        }
      } catch (error) {
        console.error("Erro ao conectar-se ao backend:", error);
        alert("Erro ao se conectar ao servidor.");
      }
    });
  });
  