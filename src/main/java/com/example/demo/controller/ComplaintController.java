package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ComplaintService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/complaints")
@PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
public class ComplaintController {

    private final ComplaintService complaintService;
    private final UserRepository userRepository;

    public ComplaintController(ComplaintService complaintService, UserRepository userRepository) {
        this.complaintService = complaintService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<ComplaintResponse> createComplaint(
            @Valid @RequestBody CreateComplaintRequest request,
            Authentication authentication) {
        Long userId = extractUserIdFromAuth(authentication);
        ComplaintResponse response = complaintService.createComplaint(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my")
    public ResponseEntity<PagedResponse<ComplaintResponse>> getMyComplaints(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        Long userId = extractUserIdFromAuth(authentication);
        Pageable pageable = PageRequest.of(page, size);
        PagedResponse<ComplaintResponse> response = complaintService.getMyComplaints(userId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComplaintResponse> getComplaintById(
            @PathVariable Long id,
            Authentication authentication) {
        Long userId = extractUserIdFromAuth(authentication);
        ComplaintResponse response = complaintService.getComplaintByIdAndUserId(id, userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComplaintResponse> updateComplaint(
            @PathVariable Long id,
            @Valid @RequestBody UpdateComplaintRequest request,
            Authentication authentication) {
        Long userId = extractUserIdFromAuth(authentication);
        ComplaintResponse response = complaintService.updateComplaint(id, userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComplaint(
            @PathVariable Long id,
            Authentication authentication) {
        Long userId = extractUserIdFromAuth(authentication);
        complaintService.deleteComplaint(id, userId);
        return ResponseEntity.ok("Complaint deleted successfully");
    }

    private Long extractUserIdFromAuth(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .map(user -> user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
