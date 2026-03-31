package com.medcheck.service;

import org.springframework.stereotype.Service;
import com.medcheck.repository.MedicineRepository;
import com.medcheck.repository.ReportRepository;
import com.medcheck.model.Medicine;
import com.medcheck.model.Report;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    private final MedicineRepository repository;
    private final ReportRepository reportRepository;
    
    public ReportService(MedicineRepository repository, ReportRepository reportRepository) {
        this.repository = repository;
        this.reportRepository = reportRepository;
    }
 
    // Hàm lưu báo cáo từ người dùng
    public void saveReport(Report report) {
        reportRepository.save(report);
    }

      // Hàm lấy tất cả báo cáo cho Admin
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }   

    // Hàm để Admin xác nhận thuốc giả và thu hồi
    public void verifyFakeMedicine(Long reportId) {
    Report report = reportRepository.findById(reportId).orElse(null);
    if (report != null) {
        report.setStatus("VERIFIED");
        report.setProcessedDate(LocalDateTime.now());
        reportRepository.save(report);
        
        // Chỉ cập nhật trạng thái nếu thuốc này có tồn tại trong hệ thống của mình
        if (report.getMedicineId() != null) {
            Medicine medicine = repository.findById(report.getMedicineId()).orElse(null);
            if (medicine != null) {
                medicine.setRecalled(true);
                repository.save(medicine);
            }
        }
    }
}

// Thêm hàm từ chối báo cáo
public void rejectReport(Long reportId) {
    Report report = reportRepository.findById(reportId).orElse(null);
    if (report != null) {
        report.setStatus("REJECTED");
        report.setProcessedDate(LocalDateTime.now());
        reportRepository.save(report);
    }
}
}