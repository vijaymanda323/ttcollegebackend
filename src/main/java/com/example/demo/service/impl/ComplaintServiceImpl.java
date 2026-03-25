package com.example.demo.service.impl;

import com.example.demo.dto.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.entity.Complaint;
import com.example.demo.model.entity.User;
import com.example.demo.repository.ComplaintRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ComplaintService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;

    public ComplaintServiceImpl(ComplaintRepository complaintRepository, UserRepository userRepository) {
        this.complaintRepository = complaintRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ComplaintResponse createComplaint(CreateComplaintRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Complaint complaint = Complaint.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .user(user)
                .build();

        complaint = complaintRepository.save(complaint);
        return mapToResponse(complaint);
    }

    @Override
    public ComplaintResponse getComplaintById(Long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found"));
        return mapToResponse(complaint);
    }

    @Override
    public ComplaintResponse getComplaintByIdAndUserId(Long id, Long userId) {
        Complaint complaint = complaintRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found or you don't have permission"));
        return mapToResponse(complaint);
    }

    @Override
    public PagedResponse<ComplaintResponse> getMyComplaints(Long userId, Pageable pageable) {
        Page<Complaint> page = complaintRepository.findByUserId(userId, pageable);
        return mapToPagedResponse(page);
    }

    @Override
    public PagedResponse<ComplaintResponse> getAllComplaints(Pageable pageable) {
        Page<Complaint> page = complaintRepository.findAll(pageable);
        return mapToPagedResponse(page);
    }

    @Override
    public ComplaintResponse updateComplaint(Long id, Long userId, UpdateComplaintRequest request) {
        Complaint complaint = complaintRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found or you don't have permission"));

        complaint.setTitle(request.getTitle());
        complaint.setDescription(request.getDescription());
        complaint.setCategory(request.getCategory());

        complaint = complaintRepository.save(complaint);
        return mapToResponse(complaint);
    }

    @Override
    public ComplaintResponse updateComplaintStatus(Long id, UpdateComplaintStatusRequest request) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found"));

        complaint.setStatus(request.getStatus());
        complaint = complaintRepository.save(complaint);
        return mapToResponse(complaint);
    }

    @Override
    public void deleteComplaint(Long id, Long userId) {
        Complaint complaint = complaintRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found or you don't have permission"));
        complaintRepository.delete(complaint);
    }

    @Override
    public void deleteComplaintByAdmin(Long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found"));
        complaintRepository.delete(complaint);
    }

    private ComplaintResponse mapToResponse(Complaint complaint) {
        return ComplaintResponse.builder()
                .id(complaint.getId())
                .title(complaint.getTitle())
                .description(complaint.getDescription())
                .category(complaint.getCategory())
                .status(complaint.getStatus())
                .createdAt(complaint.getCreatedAt())
                .updatedAt(complaint.getUpdatedAt())
                .userId(complaint.getUser().getId())
                .userName(complaint.getUser().getName())
                .userEmail(complaint.getUser().getEmail())
                .build();
    }

    private PagedResponse<ComplaintResponse> mapToPagedResponse(Page<Complaint> page) {
        return PagedResponse.<ComplaintResponse>builder()
                .content(page.getContent().stream().map(this::mapToResponse).toList())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}
