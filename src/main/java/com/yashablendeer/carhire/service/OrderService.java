package com.yashablendeer.carhire.service;

import com.yashablendeer.carhire.model.Car;
import com.yashablendeer.carhire.model.Order;
import com.yashablendeer.carhire.model.Status;
import com.yashablendeer.carhire.model.User;
import com.yashablendeer.carhire.repo.OrderRepository;
import lombok.extern.log4j.Log4j2;
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

/**
 * Order service
 *
 * @author yaroslava
 * @version 1.0
 */

@Log4j2
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
        log.info("Order #{} was deleted", id);
    }


    @Transactional
    public Order rejectOrder (int id, String reason) {
        Order order = orderRepository.findById(id);
        order.setDescription(reason);
        order.setStatus(Status.REJECTED);
        log.info("Order # {} was rejected by reason: {}", id, reason);
        return orderRepository.save(order);
    }

    public Order acceptOrder (int id) {
        Order order = orderRepository.findById(id);
        order.setStatus(Status.ACCEPTED);
        log.info("Order #{} was accepted", id);
        return orderRepository.save(order);
    }

    public Order payOrder (int id) {
        Order order = orderRepository.findById(id);
        order.setPayStatus(Status.PAYED);
        log.info("Order #{} was payed", id);
        return orderRepository.save(order);
    }

    public Order finishOrder (int id) {
        Order order = orderRepository.findById(id);
        order.setStatus(Status.FINISHED);
        log.info("Order #{} was finished", id);
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

    /**
     * Defines, if date range is available to order the specific car
     *
     * @param car Car which user wants to hire
     * @param start The start date of hiring
     * @param end The end date of hiring
     */

    public boolean checkDateAvailability(Car car, LocalDateTime start, LocalDateTime end) {

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

        boolean notValidDate =
                map.entrySet().stream()
                        .anyMatch(entry -> entry.getKey().isBefore(end) && entry.getValue().isAfter(start));

        return notValidDate;
    }


}
