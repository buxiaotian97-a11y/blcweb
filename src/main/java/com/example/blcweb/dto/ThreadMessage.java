package com.example.blcweb.dto;

import java.time.LocalDateTime;

public record ThreadMessage(
    Long id,
    String message,
    Long userId,
    String userName,
    LocalDateTime createdAt
) {}
