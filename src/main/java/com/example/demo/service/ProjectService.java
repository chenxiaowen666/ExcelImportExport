package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;

public interface ProjectService {
    void importExcel(MultipartFile file) throws Exception;
    void exportExcel(Integer annual, String projectType, String projectName, HttpServletResponse response) throws Exception;
}