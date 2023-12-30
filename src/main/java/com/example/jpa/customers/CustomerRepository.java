package com.example.jpa.customers;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {
    List<Customer> findByLastName(String lastName);

    Customer findById(long id);

    Page<Customer> findAll(Pageable paging);
}
