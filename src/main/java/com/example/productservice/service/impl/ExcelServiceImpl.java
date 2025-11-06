package com.example.productservice.service.impl;

import com.example.productservice.model.CategoryProducts;
import com.example.productservice.model.ExcelFile;
import com.example.productservice.model.Product;
import com.example.productservice.repository.ExcelFileRepository;
import com.example.productservice.service.ExcelService;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExcelServiceImpl implements ExcelService {
    @Autowired
    private ExcelFileRepository excelFileRepository;

    @Override
    public ResponseEntity<String> uploadExcelToDb(String id, MultipartFile file) throws IOException {
        byte[] fileData = file.getBytes();
        ExcelFile excelFile = new ExcelFile();
        excelFile.setFileData(fileData);
        excelFile.setFilename(file.getOriginalFilename());
        excelFile.setUploadDate(LocalDate.now());
        excelFile.setId(id);
        excelFileRepository.save(excelFile);
        return ResponseEntity.ok("Excel file uploaded successfully!");
    }

    @Override
    public ResponseEntity<String> viewExcelFromDB(String id) {
        ExcelFile latestExcel = excelFileRepository.findById(id)
                .orElse(null); // Якщо не знайдено, то буде null

        if (latestExcel != null) {
            byte[] excelContent = latestExcel.getFileData();
            String htmlContent = convertExcelToHtml(excelContent, id);

            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .header(HttpHeaders.CONTENT_TYPE, "text/html; charset=UTF-8")
                    .body(htmlContent);
        } else {
            String errorMessage = "<html><body><h1>Файл не знайдено</h1><p>Останній завантажений прайс відсутній.</p></body></html>";
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_HTML)
                    .header(HttpHeaders.CONTENT_TYPE, "text/html; charset=UTF-8")
                    .body(errorMessage);
        }
    }

    @Override
    public ResponseEntity<ByteArrayResource> downloadExcel(String id) {
        Optional<ExcelFile> excelFileOptional = excelFileRepository.findById(id);

        if (excelFileOptional.isPresent()) {
            ExcelFile excelFile = excelFileOptional.get();
            ByteArrayResource resource = new ByteArrayResource(excelFile.getFileData());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + id + ".xlsx")
                    .contentType(org.springframework.http.MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(resource);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private String convertExcelToHtml(byte[] fileData, String fileName) {
        StringBuilder html = new StringBuilder();
        try (InputStream inputStream = new ByteArrayInputStream(fileData)) {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            html.append("<html><head><style>")
                    .append("body { font-family: Arial, sans-serif; margin: 0; padding-top: 60px; background-color: #f8f9fa; text-align: center; }")
                    .append(".table-container { width: 90%; max-width: 1200px; margin: auto; overflow-x: auto; background: white; padding: 15px;")
                    .append("border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); }")
                    .append("table { width: 100%; border-collapse: collapse; margin-top: 10px; }")
                    .append("th { background-color: #6F4E37; color: white; padding: 12px; text-align: left; font-size: 16px; }")
                    .append("td { padding: 10px; border: 1px solid #ddd; font-size: 14px; }")
                    .append("tr:nth-child(even) { background-color: #f2f2f2; }")
                    .append("tr:nth-child(odd) { background-color: #ffffff; }")
                    .append("tr:first-child th { position: sticky; top: 60px; background: #6F4E37; color: white; z-index: 1; }")
                    .append(".category-row { background: #d1bfa3; font-weight: bold; text-transform: uppercase; font-size: 15px; }")
                    .append(".download-bar { position: fixed; top: 0; width: 100%; background: #6F4E37; color: white; padding: 10px 0; text-align: center; z-index: 2; }")
                    .append(".download-bar a { color: white; text-decoration: none; font-size: 18px; padding: 10px 20px; background: #4B3621; border-radius: 5px; transition: 0.3s; }")
                    .append(".download-bar a:hover { background: #3a2617; }")
                    .append(".promo-row { background: #d2b48c; font-weight: bold; }") // Світло-коричневий фон (теплий)
                    .append("@keyframes blink {")
                    .append("  0% { background: #d2b48c; }") // Початковий відтінок (тепло-коричневий)
                    .append("  50% { background: #b8865b; }") // Темніший теплий коричневий
                    .append("  100% { background: #d2b48c; }") // Повернення до початкового
                    .append("}")
                    .append(".promo-row { animation: blink 2s infinite alternate; }") // Плавне блимання кожні 2 секунди
                    .append("</style></head><body>");

            // Додаємо панель завантаження
            html.append("<div class='download-bar'>")
                    .append("<a href='/excel/download/" + fileName + "' download>⬇ Завантажити Excel</a>")
                    .append("</div>");

            html.append("<div class='table-container'><table>");

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                XSSFSheet sheet = workbook.getSheetAt(i);
                int rowIndex = 0;

                for (Row row : sheet) {
                    if (rowIndex < 4) {
                        rowIndex++;
                        continue;
                    }

                    if (rowIndex == 4) {
                        html.append("<thead>");
                    }

                    boolean isPromo = false; // Перевіряємо, чи є в рядку слово "акція"
                    Cell promoCell = row.getCell(4); // 4-та комірка (індекс 3)
                    if (promoCell != null && promoCell.getCellType() == CellType.STRING) {
                        String cellValue = promoCell.getStringCellValue().trim().toLowerCase();
                        if (cellValue.contains("акція")) {
                            isPromo = true;
                        }
                    }
                    html.append("<tr" + (isPromo ? " class='promo-row'" : "") + ">");

                    for (int cellIndex = 2; cellIndex < row.getPhysicalNumberOfCells(); cellIndex++) {
                        Cell cell = row.getCell(cellIndex);
                        html.append("<td>");
                        if (cell != null) {
                            switch (cell.getCellType()) {
                                case STRING:
                                    html.append(cell.getStringCellValue());
                                    break;
                                case NUMERIC:
                                    html.append(cell.getNumericCellValue());
                                    break;
                                case BOOLEAN:
                                    html.append(cell.getBooleanCellValue());
                                    break;
                                default:
                                    html.append("");
                            }
                        } else {
                            html.append("");
                        }
                        html.append("</td>");
                    }

                    html.append("</tr>");

                    if (rowIndex == 4) {
                        html.append("</thead>");
                    }

                    rowIndex++;
                }

            }

            html.append("</table></div></body></html>");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return html.toString();
    }





    private boolean isMergedCell(Sheet sheet, int rowIndex, int colIndex) {
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress region = sheet.getMergedRegion(i);
            if (region.isInRange(rowIndex, colIndex)) {
                return true;
            }
        }
        return false;
    }

    private String getStringValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue().trim();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((int) cell.getNumericCellValue());
        }
        return "";
    }

    private double getDoubleValue(Cell cell) {
        if (cell == null) return 0.0;
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return Double.parseDouble(cell.getStringCellValue().replace(",", ".").trim());
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        return 0.0;
    }


}
