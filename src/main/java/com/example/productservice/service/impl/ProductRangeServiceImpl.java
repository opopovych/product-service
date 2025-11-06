package com.example.productservice.service.impl;

import com.example.productservice.model.CategoryProducts;
import com.example.productservice.model.ExcelFile;
import com.example.productservice.model.Product;
import com.example.productservice.repository.ExcelFileRepository;
import com.example.productservice.service.ProductRangeService;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
public class ProductRangeServiceImpl implements ProductRangeService {
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


    private String convertExcelToHtml(byte[] fileData, String fileName) {
        StringBuilder html = new StringBuilder();
        try (InputStream inputStream = new ByteArrayInputStream(fileData)) {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            html.append("<html><head><style>")
                    .append("body { font-family: Arial, sans-serif; margin: 0; padding-top: 60px; background-color: #f8f9fa; text-align: center; }")
                    .append(".table-container { width: 90%; max-width: 800px; margin: auto; overflow-x: auto; background: white; padding: 15px;")
                    .append("border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); }")
                    .append("table { width: 100%; border-collapse: collapse; margin-top: 10px; }")
                    .append("td { padding: 10px; border: 1px solid #ddd; font-size: 14px; text-align: left; }")
                    .append(".category-row { background: #d1bfa3; font-weight: bold; text-transform: uppercase; font-size: 15px; }")
                    .append(".download-bar { position: fixed; top: 0; width: 100%; background: #6F4E37; color: white; padding: 10px 0; text-align: center; z-index: 2; }")
                    .append(".download-bar a { color: white; text-decoration: none; font-size: 18px; padding: 10px 20px; background: #4B3621; border-radius: 5px; transition: 0.3s; }")
                    .append(".download-bar a:hover { background: #3a2617; }")
                    .append("</style></head><body>");

            // Панель завантаження
            html.append("<div class='download-bar'>")
                    .append("<span>📦 Асортимент наших продуктів</span>")
                    .append("</div>");


            html.append("<div class='table-container'><table>");

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                XSSFSheet sheet = workbook.getSheetAt(i);
                int rowIndex = 0;

                for (Row row : sheet) {
                    if (rowIndex < 6) { // Пропускаємо службові рядки
                        rowIndex++;
                        continue;
                    }

                    Cell nameCell = row.getCell(4); // колонка з повною назвою товару
                    if (nameCell == null || nameCell.getCellType() != CellType.STRING) {
                        rowIndex++;
                        continue;
                    }

                    String nameValue = nameCell.getStringCellValue().trim();

                    // Категорія
                    if (isMergedCell(sheet, row.getRowNum(), 4)) {
                        html.append("<tr class='category-row'><td colspan='1'>").append(nameValue).append("</td></tr>");
                    } else {
                        // Товар
                        html.append("<tr><td>").append(nameValue).append("</td></tr>");
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
    public List<CategoryProducts> parseExcelToCategoryProducts(byte[] fileData) {
        List<CategoryProducts> result = new ArrayList<>();

        try (InputStream inputStream = new ByteArrayInputStream(fileData)) {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                XSSFSheet sheet = workbook.getSheetAt(i);
                int rowIndex = 0;
                CategoryProducts currentCategory = null;

                for (Row row : sheet) {
                    if (rowIndex < 6) { // Пропускаємо службові рядки
                        rowIndex++;
                        continue;
                    }

                    Cell nameCell = row.getCell(4); // Припускаю, що повна назва товару у 4-й колонці (індекс 3)

                    // Якщо клітинка порожня — пропускаємо
                    if (nameCell == null || nameCell.getCellType() != CellType.STRING) {
                        rowIndex++;
                        continue;
                    }

                    String cellValue = nameCell.getStringCellValue().trim();

                    // Якщо це категорія (наприклад, об'єднана комірка)
                    if (isMergedCell(sheet, row.getRowNum(), 4)) {
                        currentCategory = new CategoryProducts(cellValue);
                        result.add(currentCategory);
                    } else {
                        // Це товар — додаємо тільки назву
                        if (currentCategory != null) {
                            Product product = new Product();
                            product.setName(cellValue);
                            currentCategory.addProduct(product);
                        }
                    }

                    rowIndex++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
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
