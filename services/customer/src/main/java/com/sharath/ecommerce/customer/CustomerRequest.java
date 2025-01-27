package com.sharath.ecommerce.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(String id,
                              @NotNull(message = "Customer First Name is Required")
                              String firstName,
                              @NotNull(message = "Customer Last Name is Required")
                              String lastName,
                              @NotNull(message = "Customer email is Required")
                              @Email(message = "Email received is not a valid email address")
                              String email,
                              Address address) {


}
