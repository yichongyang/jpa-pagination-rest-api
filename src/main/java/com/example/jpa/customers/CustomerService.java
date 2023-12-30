package com.example.jpa.customers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Configuration
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    @CacheEvict(value = {"customer/id", "customer/lastName", "customers"}, allEntries = true)
    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @CacheEvict(value = {"customer/id", "customer/lastName", "customers"}, allEntries = true)
    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Cacheable("customers")
    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Cacheable("customer/lastName")
    public List<Customer> findByLastName(String lastName) {
        return customerRepository.findByLastName(lastName);
    }

    @CacheEvict(value = {"customer/id", "customer/lastName", "customers"}, allEntries = true)
    public void deleteCustomer(long id) {
        Customer customer = customerRepository.findById(id);
        customerRepository.delete(customer);
    }

    @Cacheable("customer/id")
    public Customer findById(long id) {
        return customerRepository.findById(id);
    }
}
