package edu.cit.flores.ejboy.campusequipmentloan.controller;

import edu.cit.flores.ejboy.campusequipmentloan.entity.Equipment;
import edu.cit.flores.ejboy.campusequipmentloan.service.LoanService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private final LoanService service;

    public EquipmentController(LoanService service) {
        this.service = service;
    }

    // GET /api/equipment/available
    @GetMapping("/available")
    public List<Equipment> listAvailable() {
        return service.listAvailableEquipment();
    }
}
