// Gerar PDF e salvar como Base64
function saveAsPdf() {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    // Adicione conteúdo ao PDF
    doc.text("Conteúdo do prontuário", 10, 10);

    // Converter para Base64
    const pdfBase64 = doc.output("datauristring").split(",")[1]; // Remove "data:application/pdf;base64,"

    // Dados do documento
    const documentDto = {
        fileName: "prontuario.pdf",
        contentType: "application/pdf",
        fileContent: pdfBase64
    };

    // Nome do paciente (deve ser obtido do formulário)
    const patientName = document.querySelector('input[name="nome"]').value;

    // Enviar para o back-end
    fetch(`/patients/documents?patientName=${encodeURIComponent(patientName)}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(documentDto)
    })
    .then(response => {
        if (response.ok) {
            alert("Documento salvo com sucesso!");
        } else {
            return response.text().then(error => { throw new Error(error); });
        }
    })
    .catch(error => {
        console.error("Erro ao salvar o documento:", error.message);
        alert("Erro ao salvar o documento: " + error.message);
    });
}
