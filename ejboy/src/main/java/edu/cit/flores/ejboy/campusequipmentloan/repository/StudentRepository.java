package edu.cit.flores.ejboy.campusequipmentloan.repository;

import edu.cit.flores.ejboy.campusequipmentloan.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
