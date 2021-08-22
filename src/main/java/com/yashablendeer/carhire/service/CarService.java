package com.yashablendeer.carhire.service;

import com.yashablendeer.carhire.model.Car;
import com.yashablendeer.carhire.model.Role;
import com.yashablendeer.carhire.model.Status;
import com.yashablendeer.carhire.model.User;
import com.yashablendeer.carhire.repo.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
public class CarService {
    private CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car findCarByCarMark(String carMark) {
        return carRepository.findByCarMark(carMark);
    }

    public Car findCarByCarQuality(String carQuality) {
        return carRepository.findByCarQuality(carQuality);
    }
    public Car findCarById(int id) {
        return carRepository.findById(id);
    }

    public List<Car> findAllCars() {
        return carRepository.findAll();
    }

    public Car saveCar(Car car) {
        car.setStatus(Status.READY);
        return carRepository.save(car);
    }

    //TODO refactor Car builder
    public Car updateCar(Integer id, Car car) {
        Car fromDb = findCarById(id);
        fromDb.setCarName(car.getCarName());
        fromDb.setCarMark(car.getCarMark());
        fromDb.setCarQuality(car.getCarQuality());
        fromDb.setStatus(Status.READY);
        fromDb.setCarPrice(car.getCarPrice());

        return carRepository.save(fromDb);
    }

    public Car repairHandler(Integer id) {
        Car car =  carRepository.findById(id);
        car.setStatus(Status.REPAIR);
        return carRepository.save(car);
    }
    @Transactional
    public void deleteCarById(int id) {
        carRepository.deleteById(id);
    }
}
