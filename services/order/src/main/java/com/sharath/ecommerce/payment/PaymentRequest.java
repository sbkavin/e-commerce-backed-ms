package com.sharath.ecommerce.payment;

import com.sharath.ecommerce.customer.CustomerResponse;
import com.sharath.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
