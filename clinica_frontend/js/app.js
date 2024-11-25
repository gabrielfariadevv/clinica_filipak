document.getElementById('login-form').addEventListener('submit', async function (e) {
    e.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch('http://localhost:8080/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });

        if (response.ok) {
            const data = await response.json();
            localStorage.setItem('token', data.token);

            window.location.href = 'start.html';
        } else {
            alert('Login falhou. Verifique suas credenciais.');
        }
    } catch (error) {
        console.error('Erro:', error);
        alert('Ocorreu um erro ao tentar fazer login. Tente novamente mais tarde.');
    }

    
});

function logout() {
    // Remover o token de autenticação do localStorage
    localStorage.removeItem('token');
    
    // Redirecionar para a página de login
    window.location.href = 'login.html'; // Ou para a página que você usa para login
}
