package com.example.demo.service.impl;

import com.example.demo.dto.ProjectDTO;
import com.example.demo.entity.Project;
import com.example.demo.mapper.ProjectMapper;
import com.example.demo.service.ProjectService;
import com.example.demo.util.ExcelUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectMapper projectMapper;
    private final Validator validator;

    public ProjectServiceImpl(ProjectMapper projectMapper, Validator validator) {
        this.projectMapper = projectMapper;
        this.validator = validator;
    }

    @Override
    public void importExcel(MultipartFile file) throws Exception {
        List<ProjectDTO> dtos = ExcelUtil.readExcel(file.getInputStream());

        for (ProjectDTO dto : dtos) {
            Project project = convertToEntity(dto);

            // 数据校验
            Set<ConstraintViolation<Project>> violations = validator.validate(project);
            if (!violations.isEmpty()) {
                throw new IllegalArgumentException(violations.iterator().next().getMessage());
            }

            // 检查是否存在
            Project existing = projectMapper.findByProjectName(project.getProjectName());
            if (existing == null) {
                project.setCreateTime(LocalDateTime.now());
                projectMapper.insert(project);
            } else {
                project.setUpdateTime(LocalDateTime.now());
                projectMapper.update(project);
            }
        }
    }

    @Override
    public void exportExcel(Integer year, String projectType, String projectName, HttpServletResponse response) throws Exception {
        List<Project> projects = projectMapper.findByConditions(year, projectType, projectName);
        ExcelUtil.writeExcel(projects, response);
    }

    private Project convertToEntity(ProjectDTO dto) {
        Project project = new Project();
        project.setProjectName(dto.getProjectName());
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