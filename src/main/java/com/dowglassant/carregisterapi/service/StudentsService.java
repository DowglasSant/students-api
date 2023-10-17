package com.dowglassant.carregisterapi.service;

import com.dowglassant.carregisterapi.entity.Student;
import com.dowglassant.carregisterapi.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class StudentsService {

    @Autowired
    StudentRepository studentRepository;

    public Student saveStudent(Student student) {
        try {
            if(!checkIfRequiredPropertiesAreNotNull(student)){
                log.warn("[saveStudent]: Some of the information required to register students is null and void.");
                return new Student();
            }

            if(!isValidCPF(student.getCpf())) {
                log.warn("[saveStudent]: CPF provided is invalid.");
                return new Student();
            }

            Optional<Student> existentStudent = studentRepository.findByCpf(student.getCpf());

            if(existentStudent.isPresent()) {
                log.warn("[saveStudent]: There is already a student registered with the same CPF.");
                return new Student();
            }

            student.setMatricula(student.getCpf());
            student.setIdade(calculateAge(student.getDataDeNascimento()));

            Student registeredStudent = studentRepository.save(student);
            log.info("[saveStudent]: Student successfully registered with id: " + registeredStudent.getId());

            return registeredStudent;
        }

        catch (Exception e) {
            log.error("[saveStudent]: An exception occurred when trying to register the student: " + e);
            return null;
        }
    }

    public List<Student> listAllStudents() {
        try {
            List<Student> students = studentRepository.findAll();

            log.info("[listAllStudents]: Students find successfully. " + students.size() + " records found!");

            return students;
        }

        catch (Exception e) {
            log.error("[listAllStudents]: An exception occurred when trying to list all students: " + e);
            return null;
        }
    }

    private static boolean checkIfRequiredPropertiesAreNotNull(Student student) {
        if (student == null) {
            return false;
        }

        return student.getNomeCompleto() != null && student.getCpf() != null && student.getDataDeNascimento() != null && student.getEnderecoEstado() != null && student.getEnderecoCidade() != null
                && student.getEnderecoBairro() != null && student.getEnderecoRua() != null && student.getEnderecoNumero() != null && student.getNomeDaMae() != null
                && student.getContatoDoResponsavel() != null && student.getSerie() != null;
    }

    private static boolean isValidCPF(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) {
            return false;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int[] digits = new int[11];
        for (int i = 0; i < 11; i++) {
            digits[i] = Character.getNumericValue(cpf.charAt(i));
        }

        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += digits[i] * (10 - i);
        }

        int remainder = sum % 11;
        int checkDigit1 = (remainder < 2) ? 0 : (11 - remainder);

        if (digits[9] != checkDigit1) {
            return false;
        }

        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += digits[i] * (11 - i);
        }

        remainder = sum % 11;
        int checkDigit2 = (remainder < 2) ? 0 : (11 - remainder);

        return digits[10] == checkDigit2;
    }

    private static int calculateAge(String dataDeNascimento) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate dateOfBirth = LocalDate.parse(dataDeNascimento, formatter);

        LocalDate currentDate = LocalDate.now();

        Period period = Period.between(dateOfBirth, currentDate);

        return period.getYears();
    }

}
