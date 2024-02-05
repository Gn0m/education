package com.example.demo.transaction.service;

import com.example.demo.transaction.exception.NotEnoughMoneyException;
import com.example.demo.transaction.model.Customer;
import com.example.demo.transaction.model.Order;
import com.example.demo.transaction.model.Product;
import com.example.demo.transaction.model.ProductInOrder;
import com.example.demo.transaction.model.dto.OrderDTO;
import com.example.demo.transaction.repo.CustomerRepo;
import com.example.demo.transaction.repo.OrderRepo;
import com.example.demo.transaction.repo.ProductInOrdersRepo;
import com.example.demo.transaction.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.demo.json_viewer.exceptions.NotFoundUserException.notFoundUser;

@Service
public class ServiceProductOrder {
    @Qualifier(value = "productRep")
    private final ProductRepo productRepo;
    @Qualifier(value = "orderRep")
    private final OrderRepo orderRepo;
    private final CustomerRepo customerRepo;
    private final ProductInOrdersRepo productInOrdersRepo;

    public ServiceProductOrder(ProductRepo productRepo, OrderRepo orderRepo, CustomerRepo customerRepo, ProductInOrdersRepo productInOrdersRepo
    ) {
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.customerRepo = customerRepo;
        this.productInOrdersRepo = productInOrdersRepo;
    }

    @Transactional
    public String placeOrder(long customerId, OrderDTO dto) {
        productInOrdersRepo.saveAll(dto.getInOrders());

        Order order = new Order();

        Customer customer = customerRepo.findById(customerId).orElseThrow(
                notFoundUser("Пользователь {0} не найдён", customerId));

        for (ProductInOrder inOrder : dto.getInOrders()) {
            order.addInOrder(inOrder);
            order.addInProduct(inOrder);
        }

        double sum = decreaseProduct(order.getProducts());

        changeBalanceCustomer(customer, sum);


        order.setTotalAmount(sum);
        order.setCustomer(customer);

        Order save = orderRepo.save(order);

        return "Номер вашего заказа: " + save.getId();
    }


    protected void changeBalanceCustomer(Customer customer, double price) {
        double balance = customer.getBalance();

        if (balance - price < 0) {
            throw new NotEnoughMoneyException("Баланс недостаточен для списания");
        }

        customer.setBalance(
                customer.getBalance() - price
        );
    }


    protected double decreaseProduct(List<Product> products) {

        List<Long> collect = products.stream().map(Product::getId).toList();

        Set<Product> bdProducts = productRepo.findByIdIn(collect);

        bdProducts.forEach(product -> products.forEach(
                item -> {
                    if (product.getId() == item.getId()) {
                        product.setQuantity(
                                product.getQuantity() - item.getQuantity()
                        );
                    }
                }
        ));
        productRepo.saveAll(bdProducts);

        return products.stream().map(product -> product.getQuantity() * product.getPrice())
                .mapToDouble(Double::valueOf).sum();
    }

    public Order getOne(long id) {
        return orderRepo.findById(id).orElseThrow(
                notFoundUser("Заказ с {0} не найден", id)
        );
    }

    public List<Order> getAll() {
        return orderRepo.findAll();
    }


    public Order update(long id, OrderDTO updateOrder) {
        Order order = orderRepo.findById(id).orElseThrow(
                notFoundUser("Заказ с {0} не найден", id)
        );

        Customer customer = customerRepo.findById(order.getCustomer().getId()).orElseThrow(
                notFoundUser("Пользователь с {0} не найден", id)
        );

        customer.setBalance(customer.getBalance() + order.getTotalAmount());

        List<Product> productList = new ArrayList<>();
        order.getProductInOrders().forEach(
                product -> productList.add(new Product(product))
        );
        returnProduct(productList);

        productInOrdersRepo.deleteAll(order.getProductInOrders());
        productInOrdersRepo.saveAll(updateOrder.getInOrders());

        for (ProductInOrder inOrder : updateOrder.getInOrders()) {
            order.deleteProductsAndOrderProd();
            order.addInOrder(inOrder);
            order.addInProduct(inOrder);
        }

        double sum = decreaseProduct(order.getProducts());

        changeBalanceCustomer(customer, sum);

        order.setTotalAmount(sum);

        return orderRepo.save(order);
    }


    protected void returnProduct(List<Product> products) {
        List<Long> collect = products.stream().map(Product::getId).toList();

        Set<Product> bdProducts = productRepo.findByIdIn(collect);

        bdProducts.forEach(product -> products.forEach(
                item -> {
                    if (product.getId() == item.getId()) {
                        product.setQuantity(
                                product.getQuantity() + item.getQuantity()
                        );
                    }
                }
        ));

        productRepo.saveAll(bdProducts);
    }

    public void deleteOrder(long id) {
        Order order = orderRepo.findById(id).orElseThrow(
                notFoundUser("Заказ с {0} не найден", id));
        order.deleteCusAndProd();
        orderRepo.delete(order);
    }

    public Customer getCustomer(long id) {
        return customerRepo.findById(id).orElseThrow(
                notFoundUser("Пользователь с {0} не найден", id));
    }

    public Customer createCustomer(Customer customer) {
        return customerRepo.save(customer);
    }

    public Product createProduct(Product product) {
        return productRepo.save(product);
    }
}
