package com.example.productservice.controlers;

import com.example.productservice.model.ExcelFile;
import com.example.productservice.model.PdfFile;
import com.example.productservice.model.Photo;
import com.example.productservice.repository.ExcelFileRepository;
import com.example.productservice.repository.PdfFileRepository;
import com.example.productservice.repository.PhotoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PricesController {
    @Autowired
    private PdfFileRepository pdfFileRepository;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private ExcelFileRepository excelFileRepository;
    @GetMapping("/prices")
    public String getPricesData(Model model) {
        List<PdfFile> listPdf = pdfFileRepository.findAll();
        List<ExcelFile> listExcel = excelFileRepository.findAll();
        List<Photo> listPhoto = photoRepository.findAll();

        model.addAttribute("pdfFiles", listPdf);
        model.addAttribute("excelFiles", listExcel);
        model.addAttribute("photos", listPhoto);

        return "view-prices";
    }

}
