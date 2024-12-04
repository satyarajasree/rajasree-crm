package com.rajasreeit.backend.service;

import com.rajasreeit.backend.entities.Customer;
import com.rajasreeit.backend.entities.Invoice;
import com.rajasreeit.backend.entities.Property;
import com.rajasreeit.backend.repo.CustomerRepo;
import com.rajasreeit.backend.repo.InvoiceRepo;
import com.rajasreeit.backend.repo.PropertyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PurchaseService {

    @Autowired
    private CustomerRepo customerRepository;

    @Autowired
    private PropertyRepo propertyRepository;

    @Autowired
    private InvoiceRepo invoiceRepository;

    public Invoice purchaseProperty(Long customerId, Long propertyId, Double amount) {
        Customer customer = customerRepository.findById(Math.toIntExact(customerId))
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property not found"));

        // Assign property to customer
        property.setCustomer(customer);
        propertyRepository.save(property);

        // Generate invoice
        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
        invoice.setProperty(property);
        invoice.setAmount(amount);
        invoice.setDate(LocalDateTime.now());
        invoice.setStatus("Fail");

        return invoiceRepository.save(invoice);
    }
}
