 package com.medcheck.service;

import org.springframework.stereotype.Service;
import com.medcheck.repository.MedicineRepository;
import com.medcheck.repository.ReportRepository;
import com.medcheck.model.Medicine;
import com.medcheck.model.Report;

import java.util.List;

@Service
public class MedicineService {

    private final MedicineRepository repository;
    private final ReportRepository reportRepository;

    public MedicineService(MedicineRepository repository, ReportRepository reportRepository) {
        this.repository = repository;
        this.reportRepository = reportRepository;
    }

    // Tìm kiếm thuốc theo tên
    public List<Medicine> search(String keyword) {
        return repository.findByNameContainingOrCodeContaining(keyword, keyword);
    }

    // Lấy tất cả thuốc
    public List<Medicine> getAllMedicines() {
        return repository.findAll();
    }

    // Lấy chi tiết thuốc theo ID
    public Medicine getMedicine(Long id) {
        return repository.findById(id).orElse(null);
    }

    // Thêm hoặc cập nhật thuốc (Save dùng chung cho cả 2)
    public void saveMedicine(Medicine medicine) {
        repository.save(medicine);
    }

    // Xoá thuốc theo ID
    public void deleteMedicine(Long id) {
        repository.deleteById(id);
    }

}