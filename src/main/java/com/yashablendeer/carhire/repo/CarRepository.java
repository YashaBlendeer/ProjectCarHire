package com.yashablendeer.carhire.repo;

import com.yashablendeer.carhire.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository  extends JpaRepository<Car, Long> {
    Car findById(int id);
    Car findByCarMark(String carMark);
    Car findByCarQuality(String carQuality);
    //TODO upd car??
    void deleteById(int id);
    List<Car> findAll();

}
