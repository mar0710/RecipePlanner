package com.example.demo.dtos;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CommentDto {
    private Long id;
    private String content;
    private String userName;
    private LocalDate created_at;
}
