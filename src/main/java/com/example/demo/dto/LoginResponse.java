package com.example.demo.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LoginResponse {
    private String token;
    private String type;
    private Long id;
    private String name;
    private String email;
    private String role;
}
