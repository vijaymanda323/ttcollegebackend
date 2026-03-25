package com.example.demo.dto;

import com.example.demo.model.entity.ComplaintStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateComplaintStatusRequest {

    @NotNull(message = "Status is required")
    private ComplaintStatus status;
}
