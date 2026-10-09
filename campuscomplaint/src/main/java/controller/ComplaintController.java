package com.smart.campuscomplaint.controller;

import com.smart.campuscomplaint.dto.ComplaintRequest;
import com.smart.campuscomplaint.dto.ComplaintUpdateRequest;
import com.smart.campuscomplaint.entity.Complaint;
import com.smart.campuscomplaint.service.ComplaintService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @PostMapping
    public ResponseEntity<Complaint> createComplaint(
            @RequestBody ComplaintRequest request) {

        Complaint savedComplaint =
                complaintService.saveComplaint(request);

        return ResponseEntity.status(201).body(savedComplaint);
    }

    @GetMapping
    public ResponseEntity<List<Complaint>> getAllComplaints() {
        return ResponseEntity.ok(
                complaintService.getAllComplaints()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Complaint> getComplaintById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                complaintService.getComplaintById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Complaint> updateComplaint(
            @PathVariable Long id,
            @RequestBody ComplaintUpdateRequest request) {

        return ResponseEntity.ok(
                complaintService.updateComplaint(id, request)
        );
    }

    @GetMapping("/test")
    public String test() {
        return "Complaint Controller Working";
    }
}
