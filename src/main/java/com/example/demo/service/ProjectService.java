package com.example.demo.service;

import com.example.demo.service.impl.ImportResult;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;

public interface ProjectService {
    ImportResult importExcel(MultipartFile file) throws Exception;
    void exportExcel(Integer annual, String projectType, String projectName, HttpServletResponse response) throws Exception;
}