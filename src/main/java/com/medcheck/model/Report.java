package com.medcheck.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "reports") // Đặt tên table trong DB cho rõ ràng
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long medicineId;      // ID của thuốc bị báo cáo
    private String medicineName;  // Tên thuốc để admin xem nhanh
    private String reason;        // Lý do nghi ngờ (bao bì, màu sắc...)
    private String contactInfo;   // SĐT hoặc Email người báo cáo
    private String status = "PENDING"; // Trạng thái mặc định: PENDING, VERIFIED, REJECTED
    private LocalDateTime reportDate;
    private LocalDateTime processedDate;

    // 1. Constructor không tham số (Bắt buộc phải có để Hibernate hoạt động)
    public Report() {
    }

    // 2. Constructor có tham số (Để ông khởi tạo object nhanh khi code)
    public Report(Long medicineId, String medicineName, String reason, String contactInfo) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.reason = reason;
        this.contactInfo = contactInfo;
    }

    @PrePersist
    protected void onCreate() {
        this.reportDate = LocalDateTime.now();
    }

    // 3. Getter và Setter (Dùng để lấy và gán giá trị cho các thuộc tính)
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Long medicineId) {
        this.medicineId = medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public LocalDateTime getReportDate() { return reportDate; }
    public void setReportDate(LocalDateTime reportDate) { this.reportDate = reportDate; }
    public LocalDateTime getProcessedDate() { return processedDate; }
    public void setProcessedDate(LocalDateTime processedDate) { this.processedDate = processedDate; }
}