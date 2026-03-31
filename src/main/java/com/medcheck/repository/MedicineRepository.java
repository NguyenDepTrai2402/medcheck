package com.medcheck.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.medcheck.model.Medicine;

import java.util.List;
  
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    // Tìm theo tên hoặc mã thuốc
    List<Medicine> findByNameContainingOrCodeContaining(String name, String code);

}