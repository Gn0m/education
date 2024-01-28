package com.example.demo.json_viewer.service;

import com.example.demo.json_viewer.enums.Status;
import com.example.demo.json_viewer.model.Order;
import com.example.demo.json_viewer.model.Product;
import com.example.demo.json_viewer.model.User;
import com.example.demo.json_viewer.repo.UserRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.example.demo.json_viewer.exceptions.NotFoundUserException.notFoundUser;

@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User getOne(long id) {
        return userRepo.findById(id).orElseThrow(
                notFoundUser("Пользователь с {0} не найден", id)
        );
    }

    public List<User> getAll() {
        return userRepo.findAll();
    }

    public User put(User user) {
        user.getOrders().forEach(order -> order.setUser(user));
        List<Order> orders = user.getOrders();
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
        User user1 = new User("Стрыкало", "strykalo@mail.ru");
        User user2 = new User("Якубович", "ykub@gmail.ru");

        List<Product> products1 = List.of(new Product("Шляпа", new BigDecimal(10)),
                new Product("Вторая шляпа", new BigDecimal(20)));
        List<Product> products2 = List.of(new Product("Игрушечный паровоз", new BigDecimal(30)),
                new Product("Зонтик", new BigDecimal(50)));
        List<Product> products3 = List.of(new Product("Фотоаппарат", new BigDecimal(120)),
                new Product("Перчатки", new BigDecimal(20)));

        Order order1 = new Order(products1, Status.SENT, user1);
        order1.getProducts().forEach(product -> product.setOrder(order1));
        Order order2 = new Order(products2, Status.CREATED, user1);
        order2.getProducts().forEach(product -> product.setOrder(order2));
        Order order3 = new Order(products3, Status.DELIVERED, user2);
        order3.getProducts().forEach(product -> product.setOrder(order3));

        user1.setOrders(List.of(order1, order2));
        user2.setOrders(List.of(order3));
        List<User> users = List.of(user1, user2);

        userRepo.saveAll(users);
    }
}
