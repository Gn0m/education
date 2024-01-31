package com.example.demo.json_viewer.service;

import com.example.demo.json_viewer.enums.Status;
import com.example.demo.json_viewer.model.Order;
import com.example.demo.json_viewer.model.Product;
import com.example.demo.json_viewer.model.User;
import com.example.demo.json_viewer.repo.OrderRepo;
import com.example.demo.json_viewer.repo.ProductRepo;
import com.example.demo.json_viewer.repo.UserRepo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static com.example.demo.json_viewer.exceptions.NotFoundUserException.notFoundUser;

@Service
public class UserService {

    private final UserRepo userRepo;
    @Qualifier(value = "orderRepo")
    private final OrderRepo orderRepo;
    @Qualifier(value = "productRepo")
    private final ProductRepo productRepo;

    public UserService(UserRepo userRepo, OrderRepo orderRepo, ProductRepo productRepo) {
        this.userRepo = userRepo;
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
    }

    public User getOne(long id) {
        return userRepo.findById(id).orElseThrow(
                notFoundUser("Пользователь с {0} не найден", id)
        );
    }

    public List<User> getAll() {
        return userRepo.findAll();
    }

    public List<User> getAllPaging(Pageable pageable) {
        return userRepo.findAll(pageable).getContent();
    }

    public User put(User user) {

        Set<Order> orders = user.getOrders();
        orders.forEach(order ->
                order.getProducts().forEach(
                        product -> product.setOrder(order)
                ));
        return userRepo.save(user);
    }

    public void delete(long id) {
        userRepo.delete(
                userRepo.findById(id).orElseThrow(
                        notFoundUser("Пользователь с {0} не найден", id)
                ));
    }

    public User update(long id, User user) {
        User userdb = userRepo.findById(id).orElseThrow(
                notFoundUser("Пользователь с {0} не найден", id));
        userdb.setEmail(user.getEmail());
        userdb.setName(user.getName());
        return userRepo.save(userdb);
    }

    public void init() {

        Order order1 = new Order(Status.SENT);//user1
        Order order2 = new Order(Status.CREATED);//user1
        Order order3 = new Order(Status.DELIVERED);//user3

        User user1 = new User("Стрыкало", "strykalo@mail.ru");
        user1.addOrder(order1);
        user1.addOrder(order2);
        User user2 = new User("Якубович", "ykub@gmail.ru");
        user2.addOrder(order3);

        List<Product> products1 = List.of(new Product("Шляпа", new BigDecimal(10), order1),
                new Product("Вторая шляпа", new BigDecimal(20), order1));
        List<Product> products2 = List.of(new Product("Игрушечный паровоз", new BigDecimal(30), order2),
                new Product("Зонтик", new BigDecimal(50), order2));
        List<Product> products3 = List.of(new Product("Фотоаппарат", new BigDecimal(120), order3),
                new Product("Перчатки", new BigDecimal(20), order3));

        List<Order> orderList = List.of(order1, order2, order3);

        order1.setProducts(products1);
        order2.setProducts(products2);
        order3.setProducts(products3);

        orderRepo.saveAll(orderList);

        userRepo.saveAll(List.of(user1, user2));

        productRepo.saveAll(products1);
        productRepo.saveAll(products2);
        productRepo.saveAll(products3);

    }

    public Order putOrder(Order order) {
        List<Product> products = order.getProducts();
        products.forEach(product -> {
            product.setId(0);
            product.setOrder(order);
        });
        Order save = orderRepo.save(order);
        productRepo.saveAll(products);
        return save;
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public Order getOrderById(long id) {
        return orderRepo.findById(id).orElseThrow(
                notFoundUser("Заказ с {0} не найден")
        );
    }

    public Order updateOrder(long id, Order order) {

        Order bdOrder = orderRepo.findById(id).orElseThrow(
                notFoundUser("Заказ с {0} не найден"));

        bdOrder.setStatus(order.getStatus());

        productRepo.deleteAll(bdOrder.getProducts());

        bdOrder.setProducts(order.getProducts());
        bdOrder.getProducts().forEach(product -> product.setOrder(bdOrder));

        productRepo.saveAll(bdOrder.getProducts());

        return orderRepo.save(bdOrder);
    }

    public Boolean deleteOrder(long id) {
        Order order = orderRepo.findById(id).orElseThrow(
                notFoundUser("Заказ с {0} не найден"));
        orderRepo.delete(order);
        return Boolean.TRUE;
    }
}
