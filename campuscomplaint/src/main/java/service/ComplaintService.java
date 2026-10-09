package com.smart.campuscomplaint.service;

import com.smart.campuscomplaint.dto.ComplaintRequest;
import com.smart.campuscomplaint.dto.ComplaintUpdateRequest;
import com.smart.campuscomplaint.entity.Complaint;
import com.smart.campuscomplaint.entity.User;
import com.smart.campuscomplaint.repository.ComplaintRepository;
import com.smart.campuscomplaint.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;

    public ComplaintService(
            ComplaintRepository complaintRepository,
            UserRepository userRepository) {

        this.complaintRepository = complaintRepository;
        this.userRepository = userRepository;
    }

    public Complaint saveComplaint(ComplaintRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Complaint complaint = new Complaint();

        complaint.setTitle(request.getTitle());
        complaint.setDescription(request.getDescription());
        complaint.setStatus("PENDING");

        System.out.println("STATUS = " + complaint.getStatus());

        complaint.setUser(user);

        return complaintRepository.save(complaint);
    }

    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    public Complaint getComplaintById(Long id) {

        return complaintRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Complaint not found"));
    }

    public Complaint updateComplaint(
            Long id,
            ComplaintUpdateRequest request) {

        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Complaint not found"));

        complaint.setStatus(request.getStatus());

        return complaintRepository.save(complaint);
    }
}
