package edu.cit.flores.ejboy.campusequipmentloan.repository;

import edu.cit.flores.ejboy.campusequipmentloan.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    // custom query to count active loans for a student
    long countByStudentIdAndStatus(Long studentId, Loan.Status status);
}
