package com.rajasreeit.backend.entities;


import jakarta.persistence.*;

@Entity

public class Lands {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String landType;

    private double minAmount;

    private double thirtyPercentageAmount;

    private double fullAmount;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;
}
