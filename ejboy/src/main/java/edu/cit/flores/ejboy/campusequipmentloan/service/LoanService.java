package edu.cit.flores.ejboy.campusequipmentloan.service;

import edu.cit.flores.ejboy.campusequipmentloan.entity.Equipment;
import edu.cit.flores.ejboy.campusequipmentloan.entity.Loan;
import edu.cit.flores.ejboy.campusequipmentloan.entity.Student;
import edu.cit.flores.ejboy.campusequipmentloan.repository.EquipmentRepository;
import edu.cit.flores.ejboy.campusequipmentloan.repository.LoanRepository;
import edu.cit.flores.ejboy.campusequipmentloan.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepo;
    private final EquipmentRepository equipmentRepo;
    private final StudentRepository studentRepo;

    public LoanService(LoanRepository loanRepo,
                       EquipmentRepository equipmentRepo,
                       StudentRepository studentRepo) {
        this.loanRepo = loanRepo;
        this.equipmentRepo = equipmentRepo;
        this.studentRepo = studentRepo;
    }

    /** Create a new loan with 7-day default and max-2-active rule */
    @Transactional
    public Loan createLoan(Long equipmentId, Long studentId) {
        long activeLoans = loanRepo.countByStudentIdAndStatus(studentId, Loan.Status.ACTIVE);
        if (activeLoans >= 2) {
            throw new IllegalStateException("Student already has 2 active loans");
        }

        Equipment equipment = equipmentRepo.findById(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found"));
        if (!equipment.isAvailable()) {
            throw new IllegalStateException("Equipment is not available");
        }

        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Loan loan = new Loan();
        loan.setEquipment(equipment);
        loan.setStudent(student);
        loan.setStartDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(7));
        loan.setStatus(Loan.Status.ACTIVE);

        // mark equipment unavailable
        equipment.setAvailable(false);
        equipmentRepo.save(equipment);

        return loanRepo.save(loan);
    }

    /** Return a loan and compute late penalty (₱50/day) */
    @Transactional
    public double returnLoan(Long loanId) {
        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        loan.setReturnDate(LocalDate.now());
        loan.setStatus(Loan.Status.RETURNED);

        // free the equipment
        Equipment equipment = loan.getEquipment();
        equipment.setAvailable(true);
        equipmentRepo.save(equipment);

        // late penalty
        long daysLate = ChronoUnit.DAYS.between(loan.getDueDate(), LocalDate.now());
        double penalty = daysLate > 0 ? daysLate * 50.0 : 0.0;

        loanRepo.save(loan);
        return penalty;
    }

    /** List all equipment that is currently available */
    public List<Equipment> listAvailableEquipment() {
        return equipmentRepo.findAll()
                .stream()
                .filter(Equipment::isAvailable)
                .toList();
    }
}
