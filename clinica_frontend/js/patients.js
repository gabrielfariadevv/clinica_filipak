async function fetchPatients() {
    const token = localStorage.getItem('token');
    if (!token) {
        alert('Você não está autenticado!');
        window.location.href = 'login.html'; // Redireciona para o login
        return;
    }

    try {
        const response = await fetch('http://localhost:8080/patients', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const patients = await response.json();
            renderPatientCards(patients);
        } else {
            alert('Erro ao carregar pacientes');
            console.log('Erro:', response.status);
        }
    } catch (error) {
        console.error('Erro ao buscar pacientes:', error);
        alert('Ocorreu um erro ao tentar buscar os pacientes. Tente novamente mais tarde.');
    }
}

function renderPatientCards(patients) {
    const patientCardsContainer = document.getElementById('patient-cards');
    patientCardsContainer.innerHTML = '';

    if (patients.length === 0) {
        const message = document.createElement('p');
        message.innerText = 'Não há pacientes disponíveis.';
        patientCardsContainer.appendChild(message);
    } else {
        patients.forEach(patient => {
            const card = document.createElement('div');
            card.classList.add('mb-4');
            card.innerHTML = `
                <div class="card">
                    <div class="card-header">
                        ${patient.name}
                    </div>
                    <div class="card-body">
                        <h5 class="card-title">CPF: ${patient.cpf}</h5>
                        <p class="card-text">Telefone: ${patient.telefone}</p>
                        <p class="card-text">
                            <strong>Aluno Responsável:</strong> ${patient.username || 'Não informado'}
                        </p>
                        <a href="#" class="btn btn-primary">Detalhes</a>
                    </div>
                </div>
            `;
            patientCardsContainer.appendChild(card);
        });
    }
}

// Chama a função ao carregar a página
fetchPatients();
