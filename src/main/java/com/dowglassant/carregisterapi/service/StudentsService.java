package com.dowglassant.carregisterapi.service;

import com.dowglassant.carregisterapi.entity.Student;
import com.dowglassant.carregisterapi.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

            LocalDateTime currentDate = LocalDateTime.now();

            student.setRegisterDate(currentDate);
            student.setUpdateDate(currentDate);

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

    public Student updateStudent(String id, Student student) {

        try {
            Optional<Student> existentStudentOpt = studentRepository.findById(id);

            if(existentStudentOpt.isEmpty()) {
                log.warn("[updateStudent]: No students found with the id provided.");
                return new Student();
            }

            Student existentStudent = existentStudentOpt.get();

            existentStudent.setContatoDoResponsavel(student.getContatoDoResponsavel() != null ? student.getContatoDoResponsavel() : existentStudent.getContatoDoResponsavel());

            existentStudent.setEnderecoEstado(student.getEnderecoEstado() != null ? student.getEnderecoEstado() : existentStudent.getEnderecoEstado());
            existentStudent.setEnderecoCidade(student.getEnderecoCidade() != null ? student.getEnderecoCidade() : existentStudent.getEnderecoCidade());
            existentStudent.setEnderecoBairro(student.getEnderecoBairro() != null ? student.getEnderecoBairro() : existentStudent.getEnderecoBairro());
            existentStudent.setEnderecoRua(student.getEnderecoRua() != null ? student.getEnderecoRua() : existentStudent.getEnderecoRua());
            existentStudent.setEnderecoNumero(student.getEnderecoNumero() != null ? student.getEnderecoNumero() : existentStudent.getEnderecoNumero());

            existentStudent.setSerie(student.getSerie() != null ? student.getSerie() : existentStudent.getSerie());
            existentStudent.setIdade(calculateAge(existentStudent.getDataDeNascimento()));

            existentStudent.setUpdateDate(LocalDateTime.now());

            Student updatedStudent = studentRepository.save(existentStudent);

            log.info("[updateStudent]: Student with id " + updatedStudent.getId() + " updated successfully!");

            return updatedStudent;
        }

        catch (Exception e) {
            log.error("[updateStudent]: An exception occurred when trying to update the student: " + e);
            return null;
        }
    }

    public void deleteStudent(String id) {
        try {
            Optional<Student> existentStudentOpt = studentRepository.findById(id);

            if(existentStudentOpt.isEmpty()) {
                log.warn("[updateStudent]: No students found with the id provided.");
                return;
            }

            Student existentStudent = existentStudentOpt.get();

            studentRepository.delete(existentStudent);

            log.info("[deleteStudent]: Student delete successfully.");
        }

        catch (Exception e) {
            log.error("[deleteStudent]: An exception occurred when trying to delete the student: " + e);
            return;
        }
    }

    public List<Student> listStudentsBySerie(String serie) {
        try {
            Optional<List<Student>> studentsBySerieDto = studentRepository.findBySerie(serie);

            if(studentsBySerieDto.isEmpty()){
                log.info("[listStudentsBySerie]: No students found to this serie.");
                return null;
            }

            List<Student> studentsBySerie = studentsBySerieDto.get();

            log.info("[listStudentsBySerie]: Students find successfully. " + studentsBySerie.size() + " records found!");

            return studentsBySerie;
        }

        catch (Exception e) {
            log.error("[listStudentsBySerie]: An exception occurred when trying to list students by serie: " + e);
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate dateOfBirth = LocalDate.parse(dataDeNascimento, formatter);

        LocalDate currentDate = LocalDate.now();

        Period period = Period.between(dateOfBirth, currentDate);

        return period.getYears();
    }

}
