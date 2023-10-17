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
}
