package com.example.demo.service;

import com.example.demo.dto.*;
import org.springframework.data.domain.Pageable;

public interface ComplaintService {
    ComplaintResponse createComplaint(CreateComplaintRequest request, Long userId);
    ComplaintResponse getComplaintById(Long id);
    ComplaintResponse getComplaintByIdAndUserId(Long id, Long userId);
    PagedResponse<ComplaintResponse> getMyComplaints(Long userId, Pageable pageable);
    PagedResponse<ComplaintResponse> getAllComplaints(Pageable pageable);
    ComplaintResponse updateComplaint(Long id, Long userId, UpdateComplaintRequest request);
    ComplaintResponse updateComplaintStatus(Long id, UpdateComplaintStatusRequest request);
    void deleteComplaint(Long id, Long userId);
    void deleteComplaintByAdmin(Long id);
}
