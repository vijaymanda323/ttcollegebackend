package com.example.demo.repository;

import com.example.demo.model.entity.Complaint;
import com.example.demo.model.entity.ComplaintStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    Page<Complaint> findByUserId(Long userId, Pageable pageable);
    Optional<Complaint> findByIdAndUserId(Long id, Long userId);
    Page<Complaint> findByStatus(ComplaintStatus status, Pageable pageable);
}
