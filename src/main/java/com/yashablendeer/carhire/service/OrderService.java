package com.yashablendeer.carhire.service;

import com.yashablendeer.carhire.model.Car;
import com.yashablendeer.carhire.model.Order;
import com.yashablendeer.carhire.model.Status;
import com.yashablendeer.carhire.model.User;
import com.yashablendeer.carhire.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    //TODO builder?
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

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public void deleteOrderById(int id) {
        orderRepository.deleteById(id);
    }

}
