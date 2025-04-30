package com.example.productservice.controlers;

import com.example.productservice.model.ExcelFile;
import com.example.productservice.model.PdfFile;
import com.example.productservice.model.Photo;
import com.example.productservice.repository.ExcelFileRepository;
import com.example.productservice.repository.PdfFileRepository;
import com.example.productservice.repository.PhotoRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CustomerController {
    private static LocalDateTime loadingDate;
    private static LocalDate deliveryDate;

    @Autowired
    private PdfFileRepository pdfFileRepository;
    @Autowired
    private ExcelFileRepository excelFileRepository;
    @Autowired
    private PhotoRepository photoRepository;

    @GetMapping("/viewL")
    public String openVewLPage(Model model){
        Optional<PdfFile> pdfFile1 = pdfFileRepository.findById("L");
        Optional<ExcelFile> excelFile = excelFileRepository.findById("L");
        Optional<Photo> photoFile = photoRepository.findById("L");

        model.addAttribute("pdfDate", pdfFile1.map(PdfFile::getUploadDate).orElse(null));
        model.addAttribute("excelDate", excelFile.map(ExcelFile::getUploadDate).orElse(null));
        model.addAttribute("photoDate", photoFile.map(Photo::getUploadDate).orElse(null));
        return "viewL";
    }
    @GetMapping("/viewM")
    public String openVewMPage(Model model){
        Optional<PdfFile> pdfFile1 = pdfFileRepository.findById("M");
        Optional<ExcelFile> excelFile = excelFileRepository.findById("M");
        Optional<Photo> photoFile = photoRepository.findById("M");

        model.addAttribute("pdfDate", pdfFile1.map(PdfFile::getUploadDate).orElse(null));
        model.addAttribute("excelDate", excelFile.map(ExcelFile::getUploadDate).orElse(null));
        model.addAttribute("photoDate", photoFile.map(Photo::getUploadDate).orElse(null));
        model.addAttribute("loadingDate", loadingDate);
        model.addAttribute("deliveryDate", deliveryDate);
        return "viewM";
    }
    @GetMapping("/viewS")
    public String openVewSPage(Model model){
        Optional<PdfFile> pdfFile1 = pdfFileRepository.findById("S");
        Optional<ExcelFile> excelFile = excelFileRepository.findById("S");
        Optional<Photo> photoFile = photoRepository.findById("S");

        model.addAttribute("pdfDate", pdfFile1.map(PdfFile::getUploadDate).orElse(null));
        model.addAttribute("excelDate", excelFile.map(ExcelFile::getUploadDate).orElse(null));
        model.addAttribute("photoDate", photoFile.map(Photo::getUploadDate).orElse(null));
        return "viewS";
    }
    @GetMapping("/index")
    public String openIndexPage(){
        return "index";
    }

    @PostMapping("/trip")
    public String scheduleTrip(
            @RequestParam("loadingDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newLoadingDate,
            @RequestParam("deliveryDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate newDeliveryDate,
            RedirectAttributes redirectAttributes) {

        loadingDate = newLoadingDate;
        deliveryDate = newDeliveryDate;
        return "redirect:/index";
    }
    @GetMapping("/trip")
    public String openTripPage(){
        return "trip";
    }


}
