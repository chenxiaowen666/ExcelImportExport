package com.example.demo.controller;

import com.example.demo.service.ProjectService;
import com.example.demo.service.impl.ImportResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import com.example.demo.util.ExcelUtil;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/import")
    public ResponseEntity<ImportResult> importExcel(@RequestParam("file") MultipartFile file){
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ImportResult(false, List.of("文件不能为空")));
            }
            if (!file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
                return ResponseEntity.badRequest()
                        .body(new ImportResult(false, List.of("仅支持 .xlsx 文件")));
            }
            ImportResult result = projectService.importExcel(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("导入失败: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ImportResult(false, List.of("导入失败: " + e.getMessage())));
        }
    }

    @GetMapping("/export")
    public void exportExcel(
            @RequestParam(required = false) Integer annual,
            @RequestParam(required = false) String projectType,
            @RequestParam(required = false) String projectName,
            HttpServletResponse response
    ) throws Exception {
        projectService.exportExcel(annual, projectType, projectName, response);
    }

    @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response) throws Exception {
        ExcelUtil.createTemplate(response);
    }
}