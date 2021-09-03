package com.yashablendeer.carhire.service;

import com.yashablendeer.carhire.model.*;
import com.yashablendeer.carhire.repo.CarRepository;
import com.yashablendeer.carhire.repo.OrderRepository;
import com.yashablendeer.carhire.repo.RepairRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Car service
 *
 * @author yaroslava
 * @version 1.0
 */

@Log4j2
@Service
public class CarService {
    private CarRepository carRepository;
    private OrderRepository orderRepository;
    private RepairRepository repairRepository;

    @Autowired
    public CarService(CarRepository carRepository, OrderRepository orderRepository, RepairRepository repairRepository) {
        this.carRepository = carRepository;
        this.orderRepository = orderRepository;
        this.repairRepository = repairRepository;
    }

    public List<Car> findCarsByCarMark(String carMark) {
        return carRepository.findAllByCarMark(carMark);
    }

    public List<Car> findCarsByCarQuality(String carQuality) {
        return carRepository.findAllByCarQuality(carQuality);
    }
    public Car findCarById(int id) {
        return carRepository.findById(id);
    }

    public List<Car> findAllCars() {
        return carRepository.findAll();
    }

    public Page<Car> findAllCarsPageable(PageRequest page) {
        return carRepository.findAll(page);
    }

    public Car saveCar(Car car) {
        car.setStatus(Status.READY);
        return carRepository.save(car);
    }

    public Car updateCar(Integer id, Car car) {
        Car fromDb = Car.builder()
                .id(id)
                .carName(car.getCarName())
                .carMark(car.getCarMark())
                .carQuality(car.getCarQuality())
                .carPrice(car.getCarPrice())
                .status(car.getStatus())
                .build();
        return carRepository.save(fromDb);
    }

    /**
     * Sets status "REPAIR" for car
     *
     * @param carId ID of car, which status will be changed
     * @param orderId ID of order, where repair sets
     * @param repair repair, which sets
     */

    @Transactional
    public Car repairHandler(Integer carId, Integer orderId, Repair repair) {
        Car car =  carRepository.findById(carId);
        car.setStatus(Status.REPAIR);

        repair.setOrder(orderRepository.findById(orderId));
        repair.setPayStatus(Status.UNPAYED);
        repairRepository.save(repair);
        log.info("Repair for order #{} was set", orderId);

        return carRepository.save(car);
    }

    @Transactional
    public void deleteCarById(int id) {
        List<Order> toDelete = orderRepository.findAllByCarId(id);
        toDelete.stream().map(Order::getId).forEach(idOrder -> repairRepository.deleteByOrderId(idOrder));
        orderRepository.deleteByCarId(id);
        carRepository.deleteById(id);
        log.info("A car (id = {}) was deleted", id);

    }
}
