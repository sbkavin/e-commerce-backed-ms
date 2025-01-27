package com.sharath.ecommerce.customer;

import com.sharath.ecommerce.customer.exception.CustomerNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper customerMapper;

    public String createCustomer(@Valid CustomerRequest request) {
        Customer customer = repository.save(customerMapper.toCustomer(request));
        return customer.getId();
    }

    public void updateCustomer(@Valid CustomerRequest request) {
        Customer customer = repository.findById(request.id()).orElseThrow(() -> new CustomerNotFoundException(
                String.format("Cannot update customer:: No Customer found with the provied ID:: %s", request.id())
        ));
        mergerCustomer(customer, request);
    }

    private void mergerCustomer(Customer customer, CustomerRequest request) {
        if (StringUtils.isNotBlank(request.firstName())) {
            customer.setFirstName(request.firstName());
        }
        if (StringUtils.isNotBlank(request.lastName())) {
            customer.setLastName(request.lastName());
        }
        if (StringUtils.isNotBlank(request.email())) {
            customer.setEmail(request.email());
        }
        if (request.address() != null) {
            customer.setAddress(request.address());
        }
    }

    public List<CustomerResponse> findAllCustomers() {
        return repository.findAll()
                .stream()
                .map(customerMapper::getCustomerResponse)
                .collect(Collectors.toList());
    }


    public Boolean existById(String customerId) {
        return repository.findById(customerId)
                .isPresent();
    }

    public CustomerResponse findById(String customerId) {
        return repository.findById(customerId)
                .map(customerMapper::getCustomerResponse).orElseThrow(() -> new CustomerNotFoundException(
                String.format("No customer found with the provided ID:: %s", customerId)
        ));
    }

    public void deleteCustomer(String customerId) {
        repository.deleteById(customerId);
    }
}
