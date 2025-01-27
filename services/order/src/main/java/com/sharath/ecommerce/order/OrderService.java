package com.sharath.ecommerce.order;

import com.sharath.ecommerce.customer.CustomerClient;
import com.sharath.ecommerce.kafka.OrderProducer;
import com.sharath.ecommerce.kafka.order.OrderConfirmation;
import com.sharath.ecommerce.order.exception.BusinessException;
import com.sharath.ecommerce.orderline.OrderLineRequest;
import com.sharath.ecommerce.orderline.OrderLineService;
import com.sharath.ecommerce.payment.PaymentClient;
import com.sharath.ecommerce.payment.PaymentRequest;
import com.sharath.ecommerce.product.ProductClient;
import com.sharath.ecommerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;
    @Transactional
    public Integer createOrder(OrderRequest request) {
        var customer = customerClient.findCustomerById(request.customerId()).orElseThrow(
                () -> new BusinessException("Cannot create order:: No Customer exist with the provided ID"));

        var purchasedProducts = productClient.purchaseProducts(request.products());

        var order = repository.save(mapper.toOrder(request));

        for (PurchaseRequest purchaseRequest : request.products()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity())
            );
        }
        var paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );
        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );
        log.info("Calling PAYMENT client to send payment request.");
        paymentClient.requestOrderPayment(paymentRequest);

        return order.getId();


    }

    public List<OrderResponse> findAllOrders() {
        return this.repository.findAll()
                .stream()
                .map(this.mapper::fromOrder)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Integer id) {
        return this.repository.findById(id)
                .map(this.mapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No order found with the provided ID: %d", id)));
    }
}
