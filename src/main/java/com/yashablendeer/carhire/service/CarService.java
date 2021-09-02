package com.yashablendeer.carhire.service;

import com.yashablendeer.carhire.model.*;
import com.yashablendeer.carhire.repo.CarRepository;
import com.yashablendeer.carhire.repo.OrderRepository;
import com.yashablendeer.carhire.repo.RepairRepository;
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
        Car fromDb = findCarById(id);
        fromDb.setCarName(car.getCarName());
        fromDb.setCarMark(car.getCarMark());
        fromDb.setCarQuality(car.getCarQuality());
        fromDb.setStatus(car.getStatus());
        fromDb.setCarPrice(car.getCarPrice());

        return carRepository.save(fromDb);
    }

    /**
     * Sets status "REPAIR" for car
     *
     * @param id ID of car, which status will be changed
     */

    public Car repairHandler(Integer id) {
        Car car =  carRepository.findById(id);
        car.setStatus(Status.REPAIR);
        return carRepository.save(car);
    }

    @Transactional
    public void deleteCarById(int id) {
        List<Order> toDelete = orderRepository.findAllByCarId(id);
        toDelete.stream().map(Order::getId).forEach(idOrder -> repairRepository.deleteByOrderId(idOrder));
        orderRepository.deleteByCarId(id);
        carRepository.deleteById(id);
    }
}
