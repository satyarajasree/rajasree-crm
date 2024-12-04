package com.rajasreeit.backend.repo;

import com.rajasreeit.backend.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepo extends JpaRepository<Invoice, Long> {
}
