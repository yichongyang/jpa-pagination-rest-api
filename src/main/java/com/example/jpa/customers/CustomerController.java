package com.example.jpa.customers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @GetMapping(path = "/customers")
    public List<Customer> getCustomer(@RequestParam(defaultValue = "0") Integer pageNo,
                                      @RequestParam(defaultValue = "3") Integer pageSize,
                                      @RequestParam(defaultValue = "id") String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Customer> pageResult = customerService.findAll(paging);

        if (pageResult.hasContent()) {
            return pageResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    @PostMapping(path = "/customer", consumes = "application/json", produces = "application/json")
    public Customer addCustomer(@Valid @RequestBody Customer customer) {
        return customerService.addCustomer(customer);
    }

    @PutMapping(path = "/customer/id/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Customer> updateCustomer(@PathVariable long id, @Valid @RequestBody Customer customer) {
        Customer customerToBeUpdated = customerService.findById(id);
        customerToBeUpdated.setFirstName(customer.getFirstName());
        customerToBeUpdated.setLastName(customer.getLastName());
        customerService.addCustomer(customerToBeUpdated);
        //This is just a wrapper, which is optional. The object can be returned directly.
        return ResponseEntity.ok(customerToBeUpdated);
    }

    // Request parameter for query with format as /customer?id=xxx
    @GetMapping("/customer")
    public Customer getCustomerById(@RequestParam long id) {
        return customerService.findById(id);
    }

    // Path parameter for getting
    @GetMapping(path = "/customer/id/{id}")
    public Customer getById(@PathVariable long id) {
        return customerService.findById(id);
    }

    @GetMapping(path = "/customer/lastName/{lastName}")
    public List<Customer> getByLastName(@PathVariable String lastName) {
        return customerService.findByLastName(lastName);
    }

    @DeleteMapping(path = "/customer/delete/id/{id}")
    public ResponseEntity<String> delete(@PathVariable long id) {
        Customer customer = customerService.findById(id);
        customerService.deleteCustomer(id);
        //Since we return a String message here, we use the ResponseEntity to provide the message.
        return ResponseEntity.status(HttpStatus.OK).body(customer.getFirstName() + " " + customer.getLastName() + " was deleted");
    }

    @DeleteMapping(path = "/customer/delete/lastName/{lastName}")
    public ResponseEntity<String> deleteByLastName(@PathVariable String lastName) {
        List<Customer> customers = customerService.findByLastName(lastName);
        for (Customer customer : customers) {
            customerService.deleteCustomer(customer.getId());
        }
        //Since we return a String message here, we use the ResponseEntity to provide the message.
        return ResponseEntity.status(HttpStatus.OK).body("All customers with last name " + lastName + " were deleted");
    }

    @DeleteMapping(path = "/customer/delete")
    public ResponseEntity<String> deleteAll() {
        List<Customer> customers = customerService.findAll(Pageable.unpaged()).getContent();
        for (Customer customer : customers) {
            customerService.deleteCustomer(customer.getId());
        }
        //Since we return a String message here, we use the ResponseEntity to provide the message.
        return ResponseEntity.status(HttpStatus.OK).body("All customers were deleted");
    }

    @GetMapping(path = "/customer/lastName/{lastName}/count")
    public ResponseEntity<String> countByLastName(@PathVariable String lastName) {
        List<Customer> customers = customerService.findByLastName(lastName);
        //Since we return a String message here, we use the ResponseEntity to provide the message.
        return ResponseEntity.status(HttpStatus.OK).body("There are " + customers.size() + " customers with last name " + lastName);
    }

    @GetMapping(path = "/customer/count")
    public ResponseEntity<String> count() {
        List<Customer> customers = customerService.findAll(Pageable.unpaged()).getContent();
        //Since we return a String message here, we use the ResponseEntity to provide the message.
        return ResponseEntity.status(HttpStatus.OK).body("There are " + customers.size() + " customers.");
    }

    @GetMapping(path = "/customer/lastName/{lastName}/exists")
    public ResponseEntity<String> existsByLastName(@PathVariable String lastName) {
        List<Customer> customers = customerService.findByLastName(lastName);
        //Since we return a String message here, we use the ResponseEntity to provide the message.
        return ResponseEntity.status(HttpStatus.OK).body("There are " + (customers.size() > 0 ? "" : "no ") + "customers with last name " + lastName + ".");
    }

    @GetMapping(path = "/customer/exists")
    public ResponseEntity<String> exists() {
        List<Customer> customers = customerService.findAll(Pageable.unpaged()).getContent();
        //Since we return a String message here, we use the ResponseEntity to provide the message.
        return ResponseEntity.status(HttpStatus.OK).body("There are " + (customers.size() > 0 ? "" : "no ") + "customers.");
    }

    @GetMapping(path = "/customer/lastName/{lastName}/search")
    public ResponseEntity<List<Customer>> searchByLastName(@PathVariable String lastName) {
        List<Customer> customers = customerService.findByLastName(lastName);
        //Since we return a String message here, we use the ResponseEntity to provide the message.
        return ResponseEntity.status(HttpStatus.OK).body(customers);
    }

    @GetMapping(path = "/customer/search")
    public ResponseEntity<List<Customer>> search() {
        List<Customer> customers = customerService.findAll(Pageable.unpaged()).getContent();
        //Since we return a String message here, we use the ResponseEntity to provide the message.
        return ResponseEntity.status(HttpStatus.OK).body(customers);
    }

    @GetMapping(path = "/customer/lastName/{lastName}/search/firstName/{firstName}")
    public ResponseEntity<List<Customer>> add(@PathVariable String lastName, @PathVariable String firstName) {
        List<Customer> customers = customerService.findByLastName(lastName);
        List<Customer> customersWithFirstName = new ArrayList<Customer>();
        for (Customer customer : customers) {
            if (customer.getFirstName().equals(firstName)) {
                customersWithFirstName.add(customer);
            }
        }
        //Since we return a String message here, we use the ResponseEntity to provide the message.
        return ResponseEntity.status(HttpStatus.OK).body(customersWithFirstName);
    }
}
