package com.smart.campuscomplaint.repository;

import com.smart.campuscomplaint.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
}


