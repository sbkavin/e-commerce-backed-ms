package com.sharath.ecommerce.customer;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {

    public Customer toCustomer(@Valid CustomerRequest request) {
        if(request == null) {
            return null;
        }
        return Customer.builder()
                .id(request.id())
                .address(request.address())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();
    }

    public CustomerResponse getCustomerResponse(Customer c) {
        return new CustomerResponse(c.getId(),c.getFirstName(),c.getLastName(),c.getEmail(),c.getAddress());
    }
}
