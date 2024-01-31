package com.example.demo.transaction.model.dto;

import com.example.demo.transaction.model.ProductInOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDTO {

    private List<ProductInOrder> inOrders;

    public OrderDTO() {
    }
}
