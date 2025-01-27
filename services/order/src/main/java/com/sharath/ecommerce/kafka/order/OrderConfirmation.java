package com.sharath.ecommerce.kafka.order;

import com.sharath.ecommerce.customer.CustomerResponse;
import com.sharath.ecommerce.order.PaymentMethod;
import com.sharath.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(String orderReference,
                                BigDecimal totalAmount,
                                PaymentMethod paymentMethod,
                                CustomerResponse customer,
                                List<PurchaseResponse> products) {
}
