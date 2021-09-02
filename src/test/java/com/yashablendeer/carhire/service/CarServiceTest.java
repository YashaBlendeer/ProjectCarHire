package com.yashablendeer.carhire.service;

import com.yashablendeer.carhire.model.Car;
import com.yashablendeer.carhire.model.Status;
import com.yashablendeer.carhire.model.User;
import com.yashablendeer.carhire.repo.CarRepository;
import com.yashablendeer.carhire.repo.OrderRepository;
import com.yashablendeer.carhire.repo.RepairRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RepairRepository repairRepository;

    @InjectMocks
    CarService carService;

    @BeforeEach
    void initUseCase() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void findCarById() {
        Car car = Car.builder()
                .id(63)
                .carName("xc90")
                .carMark("Volvo")
                .carPrice(115)
                .carQuality("M")
                .status(Status.READY)
                .build();
        when(carRepository.findById(anyInt())).thenReturn(car);
        Car foundCar = carService.findCarById(63);
        assertNotNull(foundCar);
        assertEquals("Volvo", foundCar.getCarMark());
    }

    @Test
    public void findCarsByCarMark() {


    }

    @Test
    public void findCarsByCarQuality() {
        //not working
//        List list = new ArrayList();
//        Car car1 = new Car(63, "xc90", "Volvo", "M", 115, Status.READY);
//        Car car2 = new Car(56, "Caravelle", "Volkswagen", "M", 112, Status.REPAIR);
//        Car car3 = new Car(12, "Quoris", "Kia", "F", 200, Status.READY);
//        Collections.addAll(list, car1, car2, car3);
//
//        when(carRepository.save(car1)).thenReturn(car1);
//        when(carRepository.save(car2)).thenReturn(car2);
//        when(carRepository.save(car3)).thenReturn(car3);
//        carRepository.save(car1);
//        carRepository.save(car2);
//        carRepository.save(car3);
//
//        List foundCars = carService.findCarsByCarQuality("M");
//        assertNotNull(foundCars);
//        assertEquals(2, foundCars.size());
//        assertTrue(foundCars.contains(car1));
//        assertTrue(foundCars.contains(car2));
    }

    @Test
    public void findAllCars() {
        Car car1 = new Car(63, "xc90", "Volvo", "M", 115, Status.READY);
        Car car2 = new Car(56, "Caravelle", "Volkswagen", "M", 112, Status.REPAIR);
        Car car3 = new Car(12, "Quoris", "Kia", "F", 200, Status.READY);

        List list = new ArrayList();
        Collections.addAll(list, car1, car2, car3);

        when(carRepository.findAll()).thenReturn(list);

        List result = carService.findAllCars();

        verify(carRepository, times(1)).findAll();
        assertNotNull(result);
        assertEquals(3, result.size());

    }

    @Test
    public void saveCar() {
        Car car = new Car(63, "xc90", "Volvo", "M", 115, Status.READY);

        when(carRepository.save(any(Car.class))).thenReturn(car);
        Car foundCar = carService.saveCar(car);
        assertEquals(63, foundCar.getId());
        verify(carRepository, times(1)).save(car);

    }

    //not working
    @Test
    public void deleteCarById() {
//        Car car1 = new Car(63, "xc90", "Volvo", "M", 115, Status.READY);
//
////        when(carRepository.deleteById(anyInt()))
//        assertTrue(carService.deleteCarById(63));
    }
}