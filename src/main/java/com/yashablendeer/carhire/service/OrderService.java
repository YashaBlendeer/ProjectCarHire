package com.yashablendeer.carhire.service;

import com.yashablendeer.carhire.model.Car;
import com.yashablendeer.carhire.model.Order;
import com.yashablendeer.carhire.model.Status;
import com.yashablendeer.carhire.model.User;
import com.yashablendeer.carhire.repo.OrderRepository;
import org.apache.el.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class OrderService {
    private OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order findOrderById(int id) {
        return orderRepository.findById(id);
    }

    public Order save (Order order) {
        return orderRepository.save(order);
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    public Page<Order> findAllOrdersByUserPageable(User user, PageRequest page) {
        return orderRepository.findAllByUser(user, page);
    }

    public Page<Order> findAllOrdersPageable(PageRequest page) {
        return orderRepository.findAll(page);
    }

    @Transactional
    public void deleteOrderById(int id) {
        orderRepository.deleteById(id);
    }

    //TODO builder?
    //TODO transactional?
    public Order rejectOrder (int id, String reason) {
        Order order = orderRepository.findById(id);
        order.setDescription(reason);
        order.setStatus(Status.REJECTED);
        return orderRepository.save(order);
    }

    public Order saveBuilder (Order order, Car car, User user) {

        //TODO check for null?
        return orderRepository.save(Order.builder()
                                    .car(car)
                                    .user(user)
                                    .passport(order.getPassport())
                                    .withDriver(order.getWithDriver())
                                    .startTime(order.getStartTime())
                                    .endTime(order.getEndTime())
                                    .orderPrice(order.getOrderPrice())
                                    .status(Status.WAITING)
                                    .payStatus(Status.UNPAYED)
                                    .description("")
                                    .build()
        );
    }


    public boolean checkDateAvailability(Car car, LocalDateTime start, LocalDateTime end) {
//        TODO minimize code in checkDateAvailability

        System.out.println("===============");
        System.out.println("inside checkDateAvailability");
        System.out.println(car);
        System.out.println("===============");
        List<LocalDateTime> startDates = orderRepository.findAll().stream()
                                                        .filter(order -> order.getCar().equals(car))
                                                        .filter(order -> order.getStatus()
                                                                        .equals(Status.ACCEPTED))
                                                        .map(Order::getStartTime)
                                                        .collect(Collectors.toList());
        List<LocalDateTime> endDates = orderRepository.findAll().stream()
                                                        .filter(order -> order.getCar().equals(car))
                                                        .filter(order -> order.getStatus()
                                                                .equals(Status.ACCEPTED))
                                                        .map(Order::getEndTime)
                                                        .collect(Collectors.toList());
        Map<LocalDateTime, LocalDateTime> map = IntStream.range(0, startDates.size())
                                                        .boxed()
                                                        .collect(Collectors.toMap(
                                                        i -> startDates.get(i), i -> endDates.get(i)));

//        TODO why not ||
        boolean notValidDate =
                map.entrySet().stream()
                        .anyMatch(entry -> entry.getKey().isBefore(end) && entry.getValue().isAfter(start));

        return notValidDate;
    }


}
