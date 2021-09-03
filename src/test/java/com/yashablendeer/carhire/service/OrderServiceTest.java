package com.yashablendeer.carhire.service;

import com.yashablendeer.carhire.model.*;
import com.yashablendeer.carhire.repo.OrderRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    OrderService orderService;

    @BeforeEach
    void initUseCase() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void findOrderById() {
        Car car = new Car(63, "xc90",
                "Volvo", "M", 115, Status.READY);
        User user = User.builder()
                .id(113)
                .active(true)
                .email("nice@gmail.com")
                .name("John")
                .lastName("Levaska")
                .userName("johnny")
                .password(bCryptPasswordEncoder.encode("johnny"))
                .roles(new HashSet<Role>(Arrays.asList(new Role(2, "USER"))))
                .build();
        Order order = new Order(13, car, user, "00000000", false, LocalDateTime.now(),
                LocalDateTime.now().plusHours(3), 600
                , Status.ACCEPTED, Status.PAYED, "");

        when(orderRepository.findById(anyInt())).thenReturn(order);
        Order foundOrder = orderService.findOrderById(13);
        assertNotNull(foundOrder);
        assertFalse(foundOrder.getWithDriver());
    }

    @Test
    public void save() {
        Car car = new Car(63, "xc90",
                "Volvo", "M", 115, Status.READY);
        User user = User.builder()
                .id(113)
                .active(true)
                .email("nice@gmail.com")
                .name("John")
                .lastName("Levaska")
                .userName("johnny")
                .password(bCryptPasswordEncoder.encode("johnny"))
                .roles(new HashSet<Role>(Arrays.asList(new Role(2, "USER"))))
                .build();
        Order order = new Order(13, car, user, "00000000", false, LocalDateTime.now(),
                LocalDateTime.now().plusHours(3), 600
                , Status.ACCEPTED, Status.PAYED, "");

        when(orderRepository.save(any(Order.class))).thenReturn(order);
        Order foundOrder = orderService.save(order);

        assertEquals(13, foundOrder.getId());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void findAllOrders() {

        Car car = new Car(63, "xc90",
                "Volvo", "M", 115, Status.READY);
        User user = User.builder()
                .id(113)
                .active(true)
                .email("nice@gmail.com")
                .name("John")
                .lastName("Levaska")
                .userName("johnny")
                .password(bCryptPasswordEncoder.encode("johnny"))
                .roles(new HashSet<Role>(Arrays.asList(new Role(2, "USER"))))
                .build();
        Order order = new Order(13, car, user, "00000000", false, LocalDateTime.now(),
                LocalDateTime.now().plusHours(3), 600
                , Status.ACCEPTED, Status.PAYED, "");

        List list = new ArrayList();
        Collections.addAll(list, order);

        when(orderRepository.findAll()).thenReturn(list);

        List result = orderService.findAllOrders();

        verify(orderRepository, times(1)).findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

}