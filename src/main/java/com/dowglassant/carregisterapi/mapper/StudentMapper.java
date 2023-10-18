package com.dowglassant.carregisterapi.mapper;

import com.dowglassant.carregisterapi.DTOs.StudentDTO;
import com.dowglassant.carregisterapi.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface StudentMapper {

    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    StudentDTO studentToStudentDTO(Student student);

    public default List<StudentDTO> studentsToStudentsDTO(List<Student> students) {
        return students.stream()
                .map(student -> new StudentDTO(
                        student.getId(),
                        student.getNomeCompleto(),
                        student.getMatricula(),
                        student.getSerie(),
                        String.valueOf(student.getIdade())
                ))
                .collect(Collectors.toList());
    }
}
