package com.a3.clinica.service;

import com.a3.clinica.model.Patient;
import com.a3.clinica.model.User;
import com.a3.clinica.repository.PatientRepository;
import com.a3.clinica.dto.PatientDTO;
import com.a3.clinica.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public PatientService(PatientRepository patientRepository, UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    public Optional<Patient> findByName(String name) throws Exception {
        Optional<Patient> patient = patientRepository.findByName(name);
        if (!patient.isPresent()) {
            throw new Exception();
        }
        return patient;
    }


    public Patient createPatient(Patient patient) throws Exception {
        User user = userRepository.findByName(patient.getName());
        if (user != null) {
            patient.setUser(user);
            return patientRepository.save(patient);
        } else {
            throw new Exception();
        }
    }


    // Busca todos os pacientes, mapeando-os para DTO, com base no usuário logado.
    public List<PatientDTO> findAll(User user) {
        List<Patient> patients;

        if (user == null || "professor".equalsIgnoreCase(user.getRole())) {
            patients = patientRepository.findAll(); // Retorna todos os pacientes para professores
        } else {
            patients = patientRepository.findByUserId(user.getId()); // Retorna os pacientes do usuário logado
        }

        return patients.stream().map(patient -> {
            PatientDTO patientDTO = new PatientDTO();
            patientDTO.setId(patient.getId());
            patientDTO.setName(patient.getName());
            patientDTO.setCpf(patient.getCpf());
            patientDTO.setTelefone(patient.getTelefone());
            if (patient.getUser() != null) {
                User userEntity = patient.getUser();
                patientDTO.setUsername(userEntity.getUsername());
                patientDTO.setRole(userEntity.getRole());
            }
            return patientDTO;
        }).collect(Collectors.toList());
    }


    public List<Patient> findByUserId(Long userId) {
        return patientRepository.findByUserId(userId);
    }
}
