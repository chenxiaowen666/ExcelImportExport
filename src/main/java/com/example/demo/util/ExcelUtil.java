package com.example.demo.util;

import com.example.demo.dto.ProjectDTO;
import com.example.demo.entity.Project;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
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
        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                throw new IllegalArgumentException("Excel 文件中没有工作表");
            }

            // 验证表头
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new IllegalArgumentException("Excel 表头不能为空");
            }
            for (int i = 0; i < HEADERS.length; i++) {
                String header = getCellStringValue(headerRow.getCell(i));
                if (!HEADERS[i].equals(header)) {
                    throw new IllegalArgumentException("表头不匹配，第" + (i + 1) + "列应为: " + HEADERS[i]);
                }
            }

            logger.info("总行数: {}", sheet.getLastRowNum());

            // 解析数据
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row)) {
                    logger.debug("第{}行为空，跳过", i + 1);
                    continue;
                }
                logger.debug("解析第{}行", i + 1);
                ProjectDTO dto = new ProjectDTO();
                try {
                    dto.setProjectName(getCellStringValue(row.getCell(0)));
                    logger.debug("项目名: {}", dto.getProjectName());
                    dto.setAnnual(getCellIntegerValue(row.getCell(1), i + 1, "年度"));
                    dto.setProjectType(getCellStringValue(row.getCell(2)));
                    dto.setProjectBudget(getCellBigDecimalValue(row.getCell(3), i + 1, "项目预算"));
                    dto.setTargetAmount(getCellBigDecimalValue(row.getCell(4), i + 1, "预算目标金额"));
                    dto.setTotalCompletionRate(getCellBigDecimalValue(row.getCell(5), i + 1, "总完成率"));
                    dto.setJanuaryRate(getCellBigDecimalValue(row.getCell(6), i + 1, "1月完成率"));
                    dto.setFebruaryRate(getCellBigDecimalValue(row.getCell(7), i + 1, "2月完成率"));
                    dto.setMarchRate(getCellBigDecimalValue(row.getCell(8), i + 1, "3月完成率"));
                    dto.setAprilRate(getCellBigDecimalValue(row.getCell(9), i + 1, "4月完成率"));
                    dto.setMayRate(getCellBigDecimalValue(row.getCell(10), i + 1, "5月完成率"));
                    dto.setJuneRate(getCellBigDecimalValue(row.getCell(11), i + 1, "6月完成率"));
                    dto.setJulyRate(getCellBigDecimalValue(row.getCell(12), i + 1, "7月完成率"));
                    dto.setAugustRate(getCellBigDecimalValue(row.getCell(13), i + 1, "8月完成率"));
                    dto.setSeptemberRate(getCellBigDecimalValue(row.getCell(14), i + 1, "9月完成率"));
                    dto.setOctoberRate(getCellBigDecimalValue(row.getCell(15), i + 1, "10月完成率"));
                    dto.setNovemberRate(getCellBigDecimalValue(row.getCell(16), i + 1, "11月完成率"));
                    dto.setDecemberRate(getCellBigDecimalValue(row.getCell(17), i + 1, "12月完成率"));
                    dto.setCreator(getCellStringValue(row.getCell(18)));
                    dto.setCreateTime(getCellDateTimeValue(row.getCell(19), i + 1, "创建时间"));
                    dto.setUpdater(getCellStringValue(row.getCell(20)));
                    dto.setUpdateTime(getCellDateTimeValue(row.getCell(21), i + 1, "修改时间"));
                    list.add(dto);
                } catch (Exception e) {
                    logger.error("解析第{}行失败: {}", i + 1, e.getMessage(), e);
                    throw new IllegalArgumentException("第" + (i + 1) + "行: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.error("读取 Excel 失败: {}", e.getMessage(), e);
            throw e;
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    logger.warn("关闭 Workbook 失败: {}", e.getMessage());
                }
            }
        }
        return list;
    }

    public static void writeExcel(List<Project> projects, HttpServletResponse response) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Projects");

        // 创建百分比格式
        DataFormat format = workbook.createDataFormat();
        CellStyle percentStyle = workbook.createCellStyle();
        percentStyle.setDataFormat(format.getFormat("0.00%")); // 格式为 85.50%

        // 创建表头
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(HEADERS[i]);
        }

        // 写入数据
        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            Row row = sheet.createRow(i + 1);
            // 非完成率字段
            row.createCell(0).setCellValue(project.getProjectName());
            row.createCell(1).setCellValue(project.getAnnual());
            row.createCell(2).setCellValue(project.getProjectType());
            row.createCell(3).setCellValue(project.getProjectBudget().doubleValue());
            row.createCell(4).setCellValue(project.getTargetAmount().doubleValue());
            row.createCell(18).setCellValue(project.getCreator());
            row.createCell(19).setCellValue(project.getCreateTime() != null ? project.getCreateTime().format(DATE_FORMATTER) : "");
            row.createCell(20).setCellValue(project.getUpdater());
            row.createCell(21).setCellValue(project.getUpdateTime() != null ? project.getUpdateTime().format(DATE_FORMATTER) : "");

            // 完成率字段（应用百分比格式）
            setPercentCell(row, 5, project.getTotalCompletionRate(), percentStyle);
            setPercentCell(row, 6, project.getJanuaryRate(), percentStyle);
            setPercentCell(row, 7, project.getFebruaryRate(), percentStyle);
            setPercentCell(row, 8, project.getMarchRate(), percentStyle);
            setPercentCell(row, 9, project.getAprilRate(), percentStyle);
            setPercentCell(row, 10, project.getMayRate(), percentStyle);
            setPercentCell(row, 11, project.getJuneRate(), percentStyle);
            setPercentCell(row, 12, project.getJulyRate(), percentStyle);
            setPercentCell(row, 13, project.getAugustRate(), percentStyle);
            setPercentCell(row, 14, project.getSeptemberRate(), percentStyle);
            setPercentCell(row, 15, project.getOctoberRate(), percentStyle);
            setPercentCell(row, 16, project.getNovemberRate(), percentStyle);
            setPercentCell(row, 17, project.getDecemberRate(), percentStyle);
        }

        // 自动调整列宽
        for (int i = 0; i < HEADERS.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // 设置响应
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=projects.xlsx");
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.flush();
    }

    // 辅助方法：设置百分比单元格
    private static void setPercentCell(Row row, int cellIndex, BigDecimal value, CellStyle percentStyle) {
        Cell cell = row.createCell(cellIndex);
        if (value != null) {
            cell.setCellValue(value.doubleValue() / 100); // 转换为小数，例如 85.50 -> 0.8550
            cell.setCellStyle(percentStyle); // 应用百分比格式
        } else {
            cell.setCellValue(0.0);
            cell.setCellStyle(percentStyle);
        }
    }

    private static String getCellStringValue(Cell cell) {
        if (cell == null) return null;
        try {
            cell.setCellType(CellType.STRING);
            return cell.getStringCellValue();
        } catch (Exception e) {
            logger.warn("获取字符串值失败: {}", e.getMessage());
            return null;
        }
    }

    private static BigDecimal getCellBigDecimalValue(Cell cell, int rowNum, String field) {
        if (cell == null) return null;
        try {
            // 先检查单元格类型
            if (cell.getCellType() == CellType.STRING) {
                String cellValue = cell.getStringCellValue();
                throw new IllegalArgumentException(field + "格式错误，需为数字，当前值: " + (cellValue.isEmpty() ? "空" : cellValue));
            }
            // 设置为数字类型并解析
            cell.setCellType(CellType.NUMERIC);
            return new BigDecimal(cell.getNumericCellValue());
        } catch (Exception e) {
            String cellValue = cell != null ? cell.toString() : "空";
            throw new IllegalArgumentException(field + "格式错误，需为数字，当前值: " + cellValue);
        }
    }

    private static Integer getCellIntegerValue(Cell cell, int rowNum, String field) {
        if (cell == null) return null;
        try {
            // 先检查单元格类型
            if (cell.getCellType() == CellType.STRING) {
                String cellValue = cell.getStringCellValue();
                throw new IllegalArgumentException(field + "格式错误，需为数字，当前值: " + (cellValue.isEmpty() ? "空" : cellValue));
            }
            cell.setCellType(CellType.NUMERIC);
            return (int) cell.getNumericCellValue();
        } catch (Exception e) {
            String cellValue = cell != null ? cell.toString() : "空";
            logger.warn("解析 {} 失败，当前值: {}", field, cellValue);
            throw new IllegalArgumentException(field + "格式错误，当前值: " + cellValue);
        }
    }

    private static LocalDateTime getCellDateTimeValue(Cell cell, int rowNum, String field) {
        if (cell == null) return null;
        try {
            return LocalDateTime.parse(cell.getStringCellValue(), DATE_FORMATTER);
        } catch (Exception e) {
            String cellValue = cell != null ? cell.toString() : "空";
            logger.warn("解析 {} 失败，当前值: {}", field, cellValue);
            throw new IllegalArgumentException(field + "格式错误，需为 yyyy-MM-dd HH:mm:ss，当前值: " + cellValue);
        }
    }

    private static boolean isRowEmpty(Row row) {
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
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