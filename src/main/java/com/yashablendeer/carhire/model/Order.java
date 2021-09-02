package com.yashablendeer.carhire.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Order entity, unidirectional connection with Car entity and User entity
 *
 * @author yaroslava
 * @version 1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private int id;

    //TODO Cascade Type?
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_id", nullable = false)
//    @NotEmpty
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
//    @NotEmpty
    private User user;

    @Column(name = "user_passport")
    @NotEmpty(message = "*Please provide passport information")
    private String passport;

    @Column(name = "withDriver")
    private Boolean withDriver;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "*Please provide a start date of ordering")
    private LocalDateTime startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "*Please provide an end date of ordering")
    private LocalDateTime endTime;

    @Column(name = "price")
    @NotNull
    private int orderPrice;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "pay_status")
    @Enumerated(EnumType.STRING)
    private Status payStatus;

    @Column(name = "description")
    private String description;

}
