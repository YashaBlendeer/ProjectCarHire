package com.yashablendeer.carhire.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "car_id")
    private int id;

    @Column(name = "name")
    @Length(min = 3, message = "*Car name must have at least 3 characters")
    @NotEmpty(message = "*Please provide a car name")
    private String carName;

    @Column(name = "mark")
    @Length(min = 2, message = "*Car mark must have at least 2 characters")
    @NotEmpty(message = "*Please provide a car mark")
    private String carMark;

    @Column(name = "quality")
    @Length(min = 1, message = "*Car quality class must have 1 letter")
    @NotEmpty(message = "*Please provide a car quality class")
    private String carQuality;

    @Column(name = "price")
    @NotNull(message = "*Please provide a car price per hour")
    private int carPrice;
}
