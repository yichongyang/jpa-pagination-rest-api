package com.example.jpa.customers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class CustomerRepositoryTests {
    @Autowired
    private CustomerRepository customerRepository;

    // JUnit test for saveCustomer
    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveCustomerTest() {
        Customer customer = new Customer("Yichong", "Yang");
        customerRepository.save(customer);
        Assertions.assertThat(customer.getId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void getCustomerTest() {
        Customer customer = customerRepository.findById(1L);
        Assertions.assertThat(customer.getId()).isEqualTo(1L);
    }

    @Test
    @Order(3)
    public void getAllCustomersTest() {
        List<Customer> customers = (List<Customer>) customerRepository.findAll();
        Assertions.assertThat(customers.size()).isGreaterThan(0);
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    public void updateCustomerTest() {
        Customer customer = customerRepository.findById(6L);
        customer.setFirstName("Shulan");
        customer.setLastName("Luo");
        Customer customerUpdated = customerRepository.save(customer);
        Assertions.assertThat(customerUpdated.getFirstName()).isEqualTo("Shulan");
    }

    @Test
    @Order(5)
    @Rollback(value = false)
    public void deleteCustomerTest() {
        Customer customer = customerRepository.findById(6L);
        customerRepository.delete(customer);
        List<Customer> customers = customerRepository.findByLastName("Luo");
        customers.forEach(c -> {
            Assertions.assertThat(c.getFirstName()).isNotEqualTo("Shulan");
        });
    }

    @Test
    @Order(6)
    public void findByLastNameTest() {
        List<Customer> customers = customerRepository.findByLastName("Yang");
        Assertions.assertThat(customers.size()).isGreaterThan(0);
    }
}