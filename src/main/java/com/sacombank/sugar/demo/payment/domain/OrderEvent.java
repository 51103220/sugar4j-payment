package com.sacombank.sugar.demo.payment.domain;

public class OrderEvent {
    private String eventId;
    private OrderStatus status;
    
    public OrderEvent(String eventId, OrderStatus status) {
        this.eventId = eventId;
        this.status = status;
    }

    public String getEventId() {
        return eventId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
