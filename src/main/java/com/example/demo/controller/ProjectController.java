package com.example.demo.controller;

import com.example.demo.service.ProjectService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import com.example.demo.util.ExcelUtil;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/import")
    public String importExcel(@RequestParam("file") MultipartFile file) throws Exception {
        projectService.importExcel(file);
        return "导入成功";
    }

    @GetMapping("/export")
    public void exportExcel(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String projectType,
            @RequestParam(required = false) String projectName,
            HttpServletResponse response
    ) throws Exception {
        projectService.exportExcel(year, projectType, projectName, response);
    }

    @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response) throws Exception {
        ExcelUtil.createTemplate(response);
    }
}