package com.dowglassant.carregisterapi.controller;

import com.dowglassant.carregisterapi.DTOs.StudentDTO;
import com.dowglassant.carregisterapi.entity.Student;
import com.dowglassant.carregisterapi.mapper.StudentMapper;
import com.dowglassant.carregisterapi.service.StudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:4200")
public class StudentController {

    @Autowired
    StudentsService studentsService;

    @PostMapping("/register")
    public ResponseEntity<StudentDTO> registerStudent(@RequestBody Student student){
        Student registeredStudent = studentsService.saveStudent(student);

        if(registeredStudent == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(registeredStudent.getId() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        StudentDTO studentDTO = StudentMapper.INSTANCE.studentToStudentDTO(registeredStudent);

        return new ResponseEntity<>(studentDTO, HttpStatus.CREATED);
    }

    @GetMapping("/find")
    public ResponseEntity<List<StudentDTO>> findAllStudents() {

        List<Student> students = studentsService.listAllStudents();

        List<StudentDTO> studentsDTO = StudentMapper.INSTANCE.studentsToStudentsDTO(students);

        return new ResponseEntity<>(studentsDTO, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable String id, @RequestBody Student updateStudent) {

        Student updatedStudent = studentsService.updateStudent(id, updateStudent);

        if(updatedStudent == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(updatedStudent.getId() == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        StudentDTO updatedStudentDTO = StudentMapper.INSTANCE.studentToStudentDTO(updatedStudent);

        return new ResponseEntity<>(updatedStudentDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id) {
        studentsService.deleteStudent(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/find/{serie}")
    public ResponseEntity<List<StudentDTO>> findStudentsBySerie(@PathVariable String serie) {

        List<Student> students = studentsService.listStudentsBySerie(serie);

        List<StudentDTO> studentsDTO = StudentMapper.INSTANCE.studentsToStudentsDTO(students);

        return new ResponseEntity<>(studentsDTO, HttpStatus.OK);
    }

}
