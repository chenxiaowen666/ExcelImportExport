package com.example.demo.entity;

import lombok.Data;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Project {
    private Long id;

    @NotBlank(message = "项目名不能为空")
    private String projectName;

    @NotBlank(message = "项目类型不能为空")
    private String projectType;

    @NotNull(message = "项目预算不能为空")
    @DecimalMin(value = "0.0", message = "项目预算不能为负数")
    private BigDecimal projectBudget;

    @NotNull(message = "预算目标金额不能为空")
    @DecimalMin(value = "0.0", message = "预算目标金额不能为负数")
    private BigDecimal targetAmount;

    @NotNull(message = "总完成率不能为空")
    @DecimalMin(value = "0.0", message = "总完成率不能为负数")
    @DecimalMax(value = "100.0", message = "总完成率不能超过100%")
    private BigDecimal totalCompletionRate;

    @NotNull(message = "1月完成率不能为空")
    @DecimalMin(value = "0.0", message = "1月完成率不能为负数")
    @DecimalMax(value = "100.0", message = "1月完成率不能超过100%")
    private BigDecimal januaryRate;

    @NotNull(message = "2月完成率不能为空")
    @DecimalMin(value = "0.0", message = "2月完成率不能为负数")
    @DecimalMax(value = "100.0", message = "2月完成率不能超过100%")
    private BigDecimal februaryRate;

    @NotNull(message = "3月完成率不能为空")
    @DecimalMin(value = "0.0", message = "3月完成率不能为负数")
    @DecimalMax(value = "100.0", message = "3月完成率不能超过100%")
    private BigDecimal marchRate;

    @NotNull(message = "4月完成率不能为空")
    @DecimalMin(value = "0.0", message = "4月完成率不能为负数")
    @DecimalMax(value = "100.0", message = "4月完成率不能超过100%")
    private BigDecimal aprilRate;

    @NotNull(message = "5月完成率不能为空")
    @DecimalMin(value = "0.0", message = "5月完成率不能为负数")
    @DecimalMax(value = "100.0", message = "5月完成率不能超过100%")
    private BigDecimal mayRate;

    @NotNull(message = "6月完成率不能为空")
    @DecimalMin(value = "0.0", message = "6月完成率不能为负数")
    @DecimalMax(value = "100.0", message = "6月完成率不能超过100%")
    private BigDecimal juneRate;

    @NotNull(message = "7月完成率不能为空")
    @DecimalMin(value = "0.0", message = "7月完成率不能为负数")
    @DecimalMax(value = "100.0", message = "7月完成率不能超过100%")
    private BigDecimal julyRate;

    @NotNull(message = "8月完成率不能为空")
    @DecimalMin(value = "0.0", message = "8月完成率不能为负数")
    @DecimalMax(value = "100.0", message = "8月完成率不能超过100%")
    private BigDecimal augustRate;

    @NotNull(message = "9月完成率不能为空")
    @DecimalMin(value = "0.0", message = "9月完成率不能为负数")
    @DecimalMax(value = "100.0", message = "9月完成率不能超过100%")
    private BigDecimal septemberRate;

    @NotNull(message = "10月完成率不能为空")
    @DecimalMin(value = "0.0", message = "10月完成率不能为负数")
    @DecimalMax(value = "100.0", message = "10月完成率不能超过100%")
    private BigDecimal octoberRate;

    @NotNull(message = "11月完成率不能为空")
    @DecimalMin(value = "0.0", message = "11月完成率不能为负数")
    @DecimalMax(value = "100.0", message = "11月完成率不能超过100%")
    private BigDecimal novemberRate;

    @NotNull(message = "12月完成率不能为空")
    @DecimalMin(value = "0.0", message = "12月完成率不能为负数")
    @DecimalMax(value = "100.0", message = "12月完成率不能超过100%")
    private BigDecimal decemberRate;

    @NotBlank(message = "创建人不能为空")
    private String creator;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String updater;
}