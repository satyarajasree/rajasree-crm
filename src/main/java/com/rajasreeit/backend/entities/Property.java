package com.rajasreeit.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "properties")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "property_id")
    private Long id;

    @Column(name = "property_name", nullable = false)
    private String propertyName;

    @Column(name = "location", nullable = false)
    private String location;

    @Lob
    @Column(name = "brochure_document")
    private byte[] brochureDocument;


    @Lob
    @Column(name = "amenities", columnDefinition = "TEXT")
    private String amenities;

    @Lob
    @Column(name = "features", columnDefinition = "TEXT")
    private String features;

    @Lob
    @Column(name = "terms_conditions", columnDefinition = "TEXT")
    private String termsConditions;


   @Lob
   @Column(name = "agreement")
   private String agreement;


    @Column(name = "property_image", columnDefinition = "LONGBLOB")
    private byte[] propertyImage;


    @Column(name = "latitude", nullable = true)
    private Double latitude;

    @Column(name = "longitude", nullable = true)
    private Double longitude;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lands> lands = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

}
