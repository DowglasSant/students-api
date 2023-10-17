package com.dowglassant.carregisterapi.mapper;

import com.dowglassant.carregisterapi.DTOs.StudentDTO;
import com.dowglassant.carregisterapi.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface StudentMapper {

    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    StudentDTO studentToStudentDTO(Student student);

    List<StudentDTO> studentsToStudentsDTO(List<Student> students);
}
