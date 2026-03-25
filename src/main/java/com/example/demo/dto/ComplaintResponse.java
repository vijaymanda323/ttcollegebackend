package com.example.demo.dto;

import com.example.demo.model.entity.ComplaintCategory;
import com.example.demo.model.entity.ComplaintStatus;
import lombok.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ComplaintResponse {
    private Long id;
    private String title;
    private String description;
    private ComplaintCategory category;
    private ComplaintStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private String userName;
    private String userEmail;
}
