package edu.cit.flores.ejboy.campusequipmentloan.controller;

import edu.cit.flores.ejboy.campusequipmentloan.entity.Loan;
import edu.cit.flores.ejboy.campusequipmentloan.service.LoanService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoanController {

    private final LoanService service;

    public LoanController(LoanService service) {
        this.service = service;
    }

    // POST /api/loans?equipmentId=1&studentId=1
    @PostMapping("/loans")
    public Loan createLoan(@RequestParam Long equipmentId,
                           @RequestParam Long studentId) {
        return service.createLoan(equipmentId, studentId);
    }

    // POST /api/loans/{id}/return
    @PostMapping("/loans/{id}/return")
    public Map<String, Object> returnLoan(@PathVariable Long id) {
        double penalty = service.returnLoan(id);
        return Map.of("loanId", id, "latePenalty", penalty);
    }
}
