package com.medcheck.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.medcheck.model.Medicine;
import com.medcheck.model.Report;
import com.medcheck.service.MedicineService;
import com.medcheck.service.ReportService;

@Controller
public class MedicineController {

    private final MedicineService service;
    private final ReportService reportService;

    public MedicineController(MedicineService service, ReportService reportService) {
        this.service = service;
        this.reportService = reportService;
    }

    // Chức năng tìm kiếm thuốc
    @GetMapping("/search")
    public String search(@RequestParam String keyword, Model model) {

        model.addAttribute("medicines", service.search(keyword));

        return "user/index";
    }

    // Trang chủ hiển thị tất cả thuốc
    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute("medicines", service.getAllMedicines());

        return "user/index";
    }

    // Trang xem chi tiết thuốc
    @GetMapping("/medicine/{id}")
    public String detail(@PathVariable Long id, Model model) {

        model.addAttribute("medicine", service.getMedicine(id));

        return "user/detail";
    }

    // Trang báo cáo thuốc giả
    @GetMapping("/report-new")
    public String reportNewForm(Model model) {
        // Truyền một object Medicine rỗng sang để Thymeleaf không bị lỗi null
        model.addAttribute("medicine", new Medicine());
        return "user/report-form";
    }

    // Xử lý gửi báo cáo
    @PostMapping("/report/submit")
    public String submitReport(@ModelAttribute Report report) {
        reportService.saveReport(report);
        return "redirect:/?success=true";
    }

    // Trang hiển thị cảnh báo thuốc giả cho người dùng
    @GetMapping("/alerts")
    public String publicAlerts(Model model) {
        // Lấy tất cả báo cáo, nhưng mình sẽ chỉ hiện những cái đã xác nhận ở ngoài giao
        // diện HTML
        model.addAttribute("reports", reportService.getAllReports());
        return "user/alerts";
    }

}