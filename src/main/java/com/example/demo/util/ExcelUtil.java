package com.example.demo.util;

import com.example.demo.dto.ProjectDTO;
import com.example.demo.entity.Project;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    private static final String[] HEADERS = {
            "项目名", "年度","项目类型", "项目预算", "预算目标金额", "总完成率",
            "1月完成率", "2月完成率", "3月完成率", "4月完成率", "5月完成率",
            "6月完成率", "7月完成率", "8月完成率", "9月完成率", "10月完成率",
            "11月完成率", "12月完成率", "创建人", "创建时间", "修改人", "修改时间"
    };

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void createTemplate(HttpServletResponse response) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Projects");
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(HEADERS[i]);
        }
        for (int i = 0; i < HEADERS.length; i++) {
            sheet.autoSizeColumn(i);
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=project_template.xlsx");
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.flush();
    }

    public static List<ProjectDTO> readExcel(InputStream inputStream) throws Exception {
        List<ProjectDTO> list = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            ProjectDTO dto = new ProjectDTO();
            dto.setProjectName(getCellStringValue(row.getCell(0)));
            dto.setAnnual(getCellIntegerValue(row.getCell(1)));
            dto.setProjectType(getCellStringValue(row.getCell(2)));
            dto.setProjectBudget(getCellBigDecimalValue(row.getCell(3)));
            dto.setTargetAmount(getCellBigDecimalValue(row.getCell(4)));
            dto.setTotalCompletionRate(getCellBigDecimalValue(row.getCell(5)));
            dto.setJanuaryRate(getCellBigDecimalValue(row.getCell(6)));
            dto.setFebruaryRate(getCellBigDecimalValue(row.getCell(7)));
            dto.setMarchRate(getCellBigDecimalValue(row.getCell(8)));
            dto.setAprilRate(getCellBigDecimalValue(row.getCell(9)));
            dto.setMayRate(getCellBigDecimalValue(row.getCell(10)));
            dto.setJuneRate(getCellBigDecimalValue(row.getCell(11)));
            dto.setJulyRate(getCellBigDecimalValue(row.getCell(12)));
            dto.setAugustRate(getCellBigDecimalValue(row.getCell(13)));
            dto.setSeptemberRate(getCellBigDecimalValue(row.getCell(14)));
            dto.setOctoberRate(getCellBigDecimalValue(row.getCell(15)));
            dto.setNovemberRate(getCellBigDecimalValue(row.getCell(16)));
            dto.setDecemberRate(getCellBigDecimalValue(row.getCell(17)));
            dto.setCreator(getCellStringValue(row.getCell(18)));
            dto.setCreateTime(getCellDateTimeValue(row.getCell(19)));
            dto.setUpdater(getCellStringValue(row.getCell(20)));
            dto.setUpdateTime(getCellDateTimeValue(row.getCell(21)));

            list.add(dto);
        }
        workbook.close();
        return list;
    }

    public static void writeExcel(List<Project> projects, HttpServletResponse response) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Projects");

        // Create header
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(HEADERS[i]);
        }

        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(project.getProjectName());
            row.createCell(1).setCellValue(project.getAnnual());
            row.createCell(2).setCellValue(project.getProjectType());
            row.createCell(3).setCellValue(project.getProjectBudget().doubleValue());
            row.createCell(4).setCellValue(project.getTargetAmount().doubleValue());
            row.createCell(5).setCellValue(project.getTotalCompletionRate().doubleValue());
            row.createCell(6).setCellValue(project.getJanuaryRate() != null ? project.getJanuaryRate().doubleValue() : 0.0);
            row.createCell(7).setCellValue(project.getFebruaryRate() != null ? project.getFebruaryRate().doubleValue() : 0.0);
            row.createCell(8).setCellValue(project.getMarchRate() != null ? project.getMarchRate().doubleValue() : 0.0);
            row.createCell(9).setCellValue(project.getAprilRate() != null ? project.getAprilRate().doubleValue() : 0.0);
            row.createCell(10).setCellValue(project.getMayRate() != null ? project.getMayRate().doubleValue() : 0.0);
            row.createCell(11).setCellValue(project.getJuneRate() != null ? project.getJuneRate().doubleValue() : 0.0);
            row.createCell(12).setCellValue(project.getJulyRate() != null ? project.getJulyRate().doubleValue() : 0.0);
            row.createCell(13).setCellValue(project.getAugustRate() != null ? project.getAugustRate().doubleValue() : 0.0);
            row.createCell(14).setCellValue(project.getSeptemberRate() != null ? project.getSeptemberRate().doubleValue() : 0.0);
            row.createCell(15).setCellValue(project.getOctoberRate() != null ? project.getOctoberRate().doubleValue() : 0.0);
            row.createCell(16).setCellValue(project.getNovemberRate() != null ? project.getNovemberRate().doubleValue() : 0.0);
            row.createCell(17).setCellValue(project.getDecemberRate() != null ? project.getDecemberRate().doubleValue() : 0.0);
            row.createCell(18).setCellValue(project.getCreator());
            row.createCell(19).setCellValue(project.getCreateTime() != null ? project.getCreateTime().format(DATE_FORMATTER) : "");
            row.createCell(20).setCellValue(project.getUpdater());
            row.createCell(21).setCellValue(project.getUpdateTime() != null ? project.getUpdateTime().format(DATE_FORMATTER) : "");
        }

        // Auto size columns
        for (int i = 0; i < HEADERS.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write to response
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=projects.xlsx");
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.flush();
    }

    private static String getCellStringValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            default -> "";
        };
    }

    private static BigDecimal getCellBigDecimalValue(Cell cell) {
        if (cell == null) return BigDecimal.ZERO;
        return switch (cell.getCellType()) {
            case NUMERIC -> new BigDecimal(cell.getNumericCellValue());
            case STRING -> {
                try {
                    yield new BigDecimal(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    yield BigDecimal.ZERO;
                }
            }
            default -> BigDecimal.ZERO;
        };
    }

    private static Integer getCellIntegerValue(Cell cell) {
        if (cell == null) return null;
        try {
            return (int) cell.getNumericCellValue();
        } catch (Exception e) {
            return null;
        }
    }

    private static LocalDateTime getCellDateTimeValue(Cell cell) {
        if (cell == null) return null;
        try {
            return LocalDateTime.parse(cell.getStringCellValue(), DATE_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    private static LocalDateTime parseDateTime(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        try {
            return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            return null;
        }
    }
}