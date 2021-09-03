package com.yashablendeer.carhire.service;

import com.yashablendeer.carhire.model.Car;
import com.yashablendeer.carhire.model.Order;
import com.yashablendeer.carhire.model.Repair;
import com.yashablendeer.carhire.model.Status;
import com.yashablendeer.carhire.repo.RepairRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Repair service
 *
 * @author yaroslava
 * @version 1.0
 */

@Log4j2
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

    public Repair findRepairByOrderId(int id) {
        return repairRepository.findRepairByOrderId(id);
    }

    public Repair payRepair (int id) {
        Repair repair = repairRepository.findRepairByOrderId(id);
        repair.setPayStatus(Status.PAYED);
        repair.getOrder().getCar().setStatus(Status.READY);
        log.info("Repair #{} was payed", id);

        return repairRepository.save(repair);
    }
}
