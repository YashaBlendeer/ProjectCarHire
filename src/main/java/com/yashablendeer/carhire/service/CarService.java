package com.yashablendeer.carhire.service;

import com.yashablendeer.carhire.model.Car;
import com.yashablendeer.carhire.model.User;
import com.yashablendeer.carhire.repo.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Car> findAllCars() {
        return carRepository.findAll();
    }
}
