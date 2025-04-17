package com.example.demo.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProjectDTO {
    private String projectName;
    private String projectType;
    private BigDecimal projectBudget;
    private BigDecimal targetAmount;
    private BigDecimal totalCompletionRate;
    private BigDecimal januaryRate;
    private BigDecimal februaryRate;
    private BigDecimal marchRate;
    private BigDecimal aprilRate;
    private BigDecimal mayRate;
    private BigDecimal juneRate;
    private BigDecimal julyRate;
    private BigDecimal augustRate;
    private BigDecimal septemberRate;
    private BigDecimal octoberRate;
    private BigDecimal novemberRate;
    private BigDecimal decemberRate;
    private String creator;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String updater;
}