package com.a3.clinica.controller;

import com.a3.clinica.dto.DocumentDto;
import com.a3.clinica.dto.PatientDTO;
import com.a3.clinica.dto.PatientRequest;
import com.a3.clinica.model.Document;
import com.a3.clinica.model.Patient;
import com.a3.clinica.model.User;
import com.a3.clinica.service.PatientService;
import com.a3.clinica.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;
    private final UserService userService;

    public PatientController(PatientService patientService, UserService userService) {
        this.patientService = patientService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<PatientDTO> patients = patientService.findAll(null); // Não filtra por usuário, retorna todos os pacientes

        if (patients.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retorna 204 se não houver pacientes
        }
        return ResponseEntity.ok(patients); // Retorna os pacientes no formato JSON
    }

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getPatientsForLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User user = userOptional.get();
        List<PatientDTO> patients = patientService.findAll(user);

        if (patients.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(patients);
    }


    @PostMapping("/create")
    public ResponseEntity<?> createPatient(@RequestBody PatientRequest patientRequest) throws Exception {
        if (patientRequest == null) {
            return ResponseEntity.badRequest().body("Dados do paciente não podem ser nulos.");
        }

        Optional<User> user = userService.findById(patientRequest.getUserId());
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }

        Patient patient = new Patient();
        patient.setName(patientRequest.getName());
        patient.setCpf(patientRequest.getCpf());
        patient.setTelefone(patientRequest.getTelefone());
        patient.setUser(user.get());

        Patient createdPatient = patientService.createPatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
    }

    @PostMapping("/documents")
    public ResponseEntity<?> addDocumentToPatientByName(
            @RequestParam String patientName,
            @RequestBody DocumentDto documentDto) throws Exception {

        // Buscar o paciente pelo nome
        Optional<Patient> patientOptional = patientService.findByName(patientName);
        Patient patient;

        if (patientOptional.isEmpty()) {
            // Se o paciente não existe, cria um novo paciente
            patient = new Patient();
            patient.setName(patientName);  // Define o nome do paciente
            // Defina aqui os outros campos do paciente (como CPF, telefone, etc.) a partir do request

            // Salva o novo paciente
            patient = patientService.createPatient(patient);
        } else {
            // Se o paciente já existe, usa o paciente encontrado
            patient = patientOptional.get();
        }

        // Criar um novo documento
        Document document = new Document();
        document.setFileName(documentDto.getFileName());
        document.setContentType(documentDto.getContentType());
        document.setFileContent(documentDto.getFileContent());
        document.setPatient(patient);

        // Adicionar o documento ao paciente
        patient.getDocuments().add(document);
        patientService.save(patient); // Salva o paciente com o novo documento

        return ResponseEntity.status(HttpStatus.CREATED).body("Documento adicionado ao paciente: " + patientName);
    }


    @GetMapping("/by-name")
    public ResponseEntity<Optional<Patient>> getPatientByName(@RequestParam String name) throws Exception {
        Optional<Patient> patients = patientService.findByName(name);
        if (patients.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(patients);
    }
}
