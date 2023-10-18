package com.dowglassant.carregisterapi.repository;

import com.dowglassant.carregisterapi.entity.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    Optional<Student> findByCpf(String cpf);

    Optional<List<Student>> findBySerie(String serie);
}
