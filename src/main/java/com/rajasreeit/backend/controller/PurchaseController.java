package com.rajasreeit.backend.controller;

import com.rajasreeit.backend.entities.Invoice;
import com.rajasreeit.backend.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @PostMapping("/property")
    public ResponseEntity<Invoice> purchaseProperty(
            @RequestParam Long customerId,
            @RequestParam Long propertyId,
            @RequestParam Double amount) {

        Invoice invoice = purchaseService.purchaseProperty(customerId, propertyId, amount);
        return ResponseEntity.ok(invoice);
    }
}
