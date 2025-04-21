package com.example.demo.service.impl;

import com.example.demo.dto.ProjectDTO;
import com.example.demo.entity.Project;
import com.example.demo.mapper.ProjectMapper;
import com.example.demo.service.ProjectService;
import com.example.demo.util.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);


    private final ProjectMapper projectMapper;
    private final Validator validator;

    public ProjectServiceImpl(ProjectMapper projectMapper, Validator validator) {
        this.projectMapper = projectMapper;
        this.validator = validator;
    }

    @Override
    public ImportResult importExcel(MultipartFile file) throws Exception {
        ImportResult result = new ImportResult();
        List<String> errors = new ArrayList<>();
        List<Project> insertList = new ArrayList<>();


        // 读取 Excel
        List<ProjectDTO> dtos = ExcelUtil.readExcel(file.getInputStream());

        // 校验重复数据
        Map<String, Integer> uniqueKeyToRow = new HashMap<>();
        for (int i = 0; i < dtos.size(); i++) {
            ProjectDTO dto = dtos.get(i);
            // 创建唯一键
            String projectName = dto.getProjectName() != null ? dto.getProjectName() : "";
            String projectType = dto.getProjectType() != null ? dto.getProjectType() : "";
            String annual = dto.getAnnual() != null ? dto.getAnnual().toString() : "";
            String uniqueKey = projectName + "|" + annual + "|" + projectType;

            // 检查重复
            if (uniqueKeyToRow.containsKey(uniqueKey)) {
                int firstRow = uniqueKeyToRow.get(uniqueKey);
                String errorMsg = String.format("第%d行与第%d行数据重复，项目名: %s，年度: %s，项目类型: %s",
                        firstRow, i + 2, projectName, annual, projectType);
                errors.add(errorMsg);
                logger.warn(errorMsg);
            } else {
                uniqueKeyToRow.put(uniqueKey, i + 2);
            }
        }
        //有重复数据就没必要往下走了
        if (!errors.isEmpty()) {
            result.setSuccess(false);
            result.setErrors(errors);
            return result;
        }
        for (int i = 0; i < dtos.size(); i++) {
            ProjectDTO dto = dtos.get(i);
            try {
                Project project = convertToEntity(dto);

//                // 设置默认值
//                if (project.getCreateTime() == null) {
//                    project.setCreateTime(LocalDateTime.now());
//                }
//                if (project.getUpdateTime() == null) {
//                    project.setUpdateTime(LocalDateTime.now());
//                }

                // 数据校验
                Set<ConstraintViolation<Project>> violations = validator.validate(project);
                if (!violations.isEmpty()) {
                    String errorMsg = violations.stream()
                            .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                            .collect(Collectors.joining("; "));
                    logger.error("处理第 {} 行数据失败: {}", i + 2, errorMsg);
                    errors.add("第" + (i + 2) + "行: " + errorMsg);
                    continue;
                }
                ProjectDTO projectDTO = new ProjectDTO();
                projectDTO.setProjectName(project.getProjectName());
                projectDTO.setAnnual(project.getAnnual());
                projectDTO.setProjectType(project.getProjectType());

                // 检查是否存在,判断插入或更新
                List<Project> existingList = projectMapper.findByConditions(projectDTO);
                if (!existingList.isEmpty()) {
                    // 删除旧数据
                    projectMapper.deleteByConditions(projectDTO);
                    insertList.add(project);
                } else {
                    insertList.add(project);
                }
            } catch (Exception e) {
                logger.error("处理第 {} 行数据失败: {}", i + 2, e.getMessage());
                errors.add("第" + (i + 2) + "行: " + e.getMessage());
            }
        }

        // 执行批量操作
        try {
            if (!insertList.isEmpty()) {
                projectMapper.batchInsert(insertList);
            }
        } catch (Exception e) {
            logger.error("批量操作失败: {}", e.getMessage());
            errors.add("批量操作失败: " + e.getMessage());
        }

        // 设置结果
        result.setSuccess(errors.isEmpty());
        result.setErrors(errors);
        return result;
    }

    @Override
    public void exportExcel(Integer annual, String projectType, String projectName, HttpServletResponse response) throws Exception {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setAnnual(annual);
        projectDTO.setProjectType(projectType);
        projectDTO.setProjectName(projectName);
        List<Project> projects = projectMapper.findByConditions(projectDTO);
        ExcelUtil.writeExcel(projects, response);
    }

    private Project convertToEntity(ProjectDTO dto) {
        Project project = new Project();
        project.setProjectName(dto.getProjectName());
        project.setAnnual(dto.getAnnual());
        project.setProjectType(dto.getProjectType());
        project.setProjectBudget(dto.getProjectBudget());
        project.setTargetAmount(dto.getTargetAmount());
        project.setTotalCompletionRate(dto.getTotalCompletionRate());
        project.setJanuaryRate(dto.getJanuaryRate());
        project.setFebruaryRate(dto.getFebruaryRate());
        project.setMarchRate(dto.getMarchRate());
        project.setAprilRate(dto.getAprilRate());
        project.setMayRate(dto.getMayRate());
        project.setJuneRate(dto.getJuneRate());
        project.setJulyRate(dto.getJulyRate());
        project.setAugustRate(dto.getAugustRate());
        project.setSeptemberRate(dto.getSeptemberRate());
        project.setOctoberRate(dto.getOctoberRate());
        project.setNovemberRate(dto.getNovemberRate());
        project.setDecemberRate(dto.getDecemberRate());
        project.setCreator(dto.getCreator());
        project.setCreateTime(dto.getCreateTime());
        project.setUpdater(dto.getUpdater());
        project.setUpdateTime(dto.getUpdateTime());

        return project;
    }
}