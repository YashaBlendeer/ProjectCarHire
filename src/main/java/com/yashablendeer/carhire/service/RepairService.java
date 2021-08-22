package com.yashablendeer.carhire.service;

import com.yashablendeer.carhire.model.Car;
import com.yashablendeer.carhire.model.Repair;
import com.yashablendeer.carhire.repo.RepairRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepairService {
    RepairRepository repairRepository;

    public RepairService(RepairRepository repairRepository) {
        this.repairRepository = repairRepository;
    }

    public Repair findRepairById (int id) {
        return repairRepository.findById(id);
    }

    public List<Repair> findAllrepairs() {
        return repairRepository.findAll();
    }

    public Repair save(Repair repair) {
        return repairRepository.save(repair);
    }
}
