package com.medcheck.controller;

import java.util.List;
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
public class AdminController {

    private final MedicineService service;
    private final ReportService reportService;

    public AdminController(MedicineService service, ReportService reportService) {
        this.service = service;
        this.reportService = reportService;
    }

    // Trang dành cho Admin xem danh sách
    @GetMapping("/admin/reports")
    public String adminReports(Model model) {
        model.addAttribute("reports", reportService.getAllReports());
        return "admin/admin-reports"; // file HTML cho admin
    }

    // Admin bấm nút xác nhận thuốc giả
    @PostMapping("/admin/reports/verify/{id}")
    public String verifyReport(@PathVariable Long id) {
        reportService.verifyFakeMedicine(id);
        return "redirect:/admin/reports";
    }

    // Admin bấm nút từ chối báo cáo (Reject)
    @PostMapping("/admin/reports/reject/{id}")
    public String rejectReport(@PathVariable Long id) {
        reportService.rejectReport(id);
        return "redirect:/admin/reports";
    }

    // 1. Trang quản lý danh sách thuốc (Nơi có nút Sửa/Xoá)
    @GetMapping("/admin/medicines")
    public String manageMedicines(Model model) {
        model.addAttribute("medicines", service.getAllMedicines());
        return "admin/admin-medicines";
    }

    // 2. Mở form thêm thuốc mới
    @GetMapping("/admin/medicines/add")
    public String addMedicineForm(Model model) {
        model.addAttribute("medicine", new Medicine());
        return "admin/medicine-form"; // Dùng chung form cho cả thêm và sửa
    }

    // 3. Mở form sửa thuốc đã có
    @GetMapping("/admin/medicines/edit/{id}")
    public String editMedicineForm(@PathVariable Long id, Model model) {
        model.addAttribute("medicine", service.getMedicine(id));
        return "admin/medicine-form";
    }

    // 4. Xử lý lưu dữ liệu (Post từ Form)
    @PostMapping("/admin/medicines/save")
    public String saveMedicine(@ModelAttribute Medicine medicine) {
        service.saveMedicine(medicine);
        return "redirect:/admin/medicines";
    }

    // 5. Xử lý xoá thuốc
    @PostMapping("/admin/medicines/delete/{id}")
    public String deleteMedicine(@PathVariable Long id) {
        service.deleteMedicine(id);
        return "redirect:/admin/medicines";
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        // Lấy số liệu thực tế từ service
        List<Medicine> allMedicines = service.getAllMedicines();
        List<Report> allReports = reportService.getAllReports();

        long pendingCount = allReports.stream().filter(r -> "PENDING".equals(r.getStatus())).count();
        long verifiedCount = allReports.stream().filter(r -> "VERIFIED".equals(r.getStatus())).count();
        long rejectedCount = allReports.stream().filter(r -> "REJECTED".equals(r.getStatus())).count();

        model.addAttribute("totalMedicines", allMedicines.size());
        model.addAttribute("pendingReports", pendingCount);
        model.addAttribute("verifiedFake", verifiedCount);
        model.addAttribute("rejectedReports", rejectedCount);

        return "admin/admin-dashboard";
    }
}