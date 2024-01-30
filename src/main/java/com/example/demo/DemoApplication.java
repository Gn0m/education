package com.example.demo;

import com.example.demo.bank_account.BankAccount;
import com.example.demo.barrier.ComplexTaskExecutor;
import com.example.demo.blocking.BlockingQueue;
import com.example.demo.blocking.Consumer;
import com.example.demo.blocking.Producer;
import com.example.demo.deadlock.DeadlockExample;
import com.example.demo.factorial.FactorialTask;
import com.example.demo.filter.Filter;
import com.example.demo.filter.FilterImplDenominator;
import com.example.demo.filter.FilterImplMultiplication;
import com.example.demo.model.Order;
import com.example.demo.model.Student;
import com.example.demo.stringbuilder.MyBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@Slf4j
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);

//        //Практическое задание - StringBuilder
//        stringbuilder();
//        //Практическое задание - Collection - фильтрация
//        collectionFilter();
//        //Практическое задание - Collection - count of elements
//        countsOfElement(new int[]{1, 2, 1, 3, 5, 0, 1, 3, 7, 5, 5, 5, 4, 2});
//        //Практическая задача - Concurrency - блокирующая очередь
//        blockingQ();
//        //Практическая задача - Concurrency - многопоточный банковский счет
//        bankAccount();
//        //Практическое задание - Concurrency
//        deadlock();
//        //Практическое задание - Concurrency - синхронизаторы
        complexTasks();
//        //Практическое задание - Stream API - генерация чисел
//        notebooks();
//        //Практическое задачние - Stream API - агрегация и объединение результатов
//        students();
//        //Практическое задание - Stream API - ForkJoinPool: Рекурсивное вычисление факториала
//        factorial();
    }

    private static void bankAccount() {
        /*
        Создайте класс BankAccount, представляющий банковский счет. Реализуйте методы deposit для внесения средств на счет и withdraw для снятия средств.
        Класс BankAccount должен содержать синхронизированные методы deposit и withdraw для обеспечения правильной синхронизации доступа к банковскому счету.
        Реализуйте метод getBalance, возвращающий текущий баланс счета.
        Создайте несколько потоков, представляющих различных клиентов банка, и дайте им возможность одновременно вносить и снимать деньги со счета.
        Предусмотрите сценарии, когда один поток пытается снять средства, когда на счете недостаточно средств.
         */
        BankAccount account = new BankAccount();

        Thread depositThread1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                account.deposit(100);
                System.out.println("Deposited: " + account.getBalance());
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread depositThread2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                account.deposit(150);
                System.out.println("Deposited: " + account.getBalance());
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread withdrawThread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                account.withdraw(80);
                System.out.println("Withdrawn: " + account.getBalance());
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Запуск потоков
        depositThread1.start();
        depositThread2.start();
        withdrawThread.start();

        try {
            // Даем потокам время выполниться
            depositThread1.join();
            depositThread2.join();
            withdrawThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void factorial() {
        /*
        Рассмотрим задачу вычисления факториала числа с использованием ForkJoinPool.
        Факториал числа n обозначается как n! и вычисляется как произведение всех положительных целых чисел от 1 до n.
        Реализуйте класс FactorialTask, который расширяет RecursiveTask. Этот класс будет выполнять рекурсивное вычисление факториала числа.
        В конструкторе FactorialTask передайте число n, факториал которого нужно вычислить.
        В методе compute() разбейте задачу на подзадачи и используйте fork() для их асинхронного выполнения.
        Используйте join() для получения результатов подзадач и комбинирования их для получения общего результата.
        В основном методе создайте экземпляр FactorialTask с числом, для которого нужно вычислить факториал, и запустите его в ForkJoinPool.
        Выведите результат вычисления факториала.
         */
        try (ForkJoinPool forkJoinPool = new ForkJoinPool()) {
            FactorialTask factorialTask = new FactorialTask(5);
            forkJoinPool.invoke(factorialTask);
        }
    }

    private static void students() {
        /*
        Создайте коллекцию студентов, где каждый студент содержит информацию о предметах, которые он изучает, и его оценках по этим предметам.
        Используйте Parallel Stream для обработки данных и создания Map, где ключ - предмет, а значение - средняя оценка по всем студентам.
        Выведите результат: общую Map с средними оценками по всем предметам.
         */
        List<Student> students = Arrays.asList(new Student(Map.of("Math", 90, "Physics", 85)),
                new Student(Map.of("Math", 95, "Physics", 88)),
                new Student(Map.of("Math", 88, "Chemistry", 92)),
                new Student(Map.of("Physics", 78, "Chemistry", 85)));

        students.parallelStream().flatMap(student -> student.getGrades().entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.averagingInt(Map.Entry::getValue)));
    }

    private static void notebooks() {
        /*
    Предположим, у нас есть список заказов, и каждый заказ представляет собой продукт и его стоимость.
    Задача состоит в использовании Stream API и коллекторов для решения следующих задач:

    -Создайте список заказов с разными продуктами и их стоимостями.
    -Группируйте заказы по продуктам.
    -Для каждого продукта найдите общую стоимость всех заказов.
    -Отсортируйте продукты по убыванию общей стоимости.
    -Выберите три самых дорогих продукта.
    -Выведите результат: список трех самых дорогих продуктов и их общая стоимость.
         */
        List<Order> orders = List.of(new Order("Laptop", 1200.0), new Order("Smartphone", 800.0), new Order("Laptop", 1500.0), new Order("Tablet", 500.0), new Order("Smartphone", 900.0));

        orders.stream().collect(Collectors.groupingBy(Order::getProduct));

        Map<String, Double> summing = orders.stream().collect(Collectors.groupingBy(Order::getProduct, Collectors.summingDouble(Order::getCost)));

        summing.entrySet().stream().sorted(Map.Entry.<String, Double>comparingByValue()
                .reversed()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Double::sum, TreeMap::new));

        orders.stream().sorted(Comparator.comparingDouble(Order::getCost).reversed()).limit(3).toList();
    }

    private static void complexTasks() {
        /*
        Синхронизация потоков с использованием CyclicBarrier и ExecutorService.
        В этой задаче мы будем использовать CyclicBarrier и ExecutorService для синхронизации нескольких потоков,
        выполняющих сложную задачу, и затем ожидающих, пока все потоки завершат выполнение, чтобы объединить результаты.
        Создайте класс ComplexTask, представляющий сложную задачу, которую несколько потоков будут выполнять.
        В каждой задаче реализуйте метод execute(), который выполняет часть сложной задачи.
        Создайте класс ComplexTaskExecutor, в котором будет использоваться CyclicBarrier и ExecutorService для синхронизации выполнения задач.
        Реализуйте метод executeTasks(int numberOfTasks), который создает пул потоков и назначает каждому потоку экземпляр сложной задачи для выполнения.
        Затем используйте CyclicBarrier для ожидания завершения всех потоков и объединения результатов их работы.
        В методе main создайте экземпляр ComplexTaskExecutor и вызовите метод executeTasks с несколькими задачами для выполнения.
         */
        ComplexTaskExecutor taskExecutor = new ComplexTaskExecutor(5);

        Runnable testRunnable = () -> {
            log.info(Thread.currentThread().getName() + " started the test.");

            taskExecutor.executeTasks(5);

            log.info(Thread.currentThread().getName() + " completed the test.");
        };

        Thread thread1 = new Thread(testRunnable, "TestThread-1");
        Thread thread2 = new Thread(testRunnable, "TestThread-2");

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            log.warn(String.valueOf(e));
            Thread.currentThread().interrupt();
        }
    }

    private static void stringbuilder() {
    /* Изучите внутреннюю реализацию класса StringBuilder
    и напишите свою с добавлением дополнительного метода - undo().
    Прежде чем приступать - прочитайте про паттерн state и примените его в своей реализации. */

        MyBuilder str = new MyBuilder("hello");
        System.out.println(str);
        str.concat(" world");
        System.out.println(str);
        str.concat(" people");
        System.out.println(str);
        str.undo();
        System.out.println(str);
    }

    private static Object[] filter(int[] arr, Filter filter) {
        return Arrays.stream(arr).mapToObj(filter::apply).toArray(Object[]::new);
    }

    private static void collectionFilter() {
        /*
        Напишите метод filter, который принимает на вход массив любого типа, вторым арументом метод должен принимать клас,
        реализующий интерфейс Filter, в котором один метод - Object apply(Object o).
        Метод должен быть реализован так чтобы возращать новый масив, к каждому элементу которого была применена функция apply
        */
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8};

        filter(arr, new FilterImplDenominator());
        filter(arr, new FilterImplMultiplication());

    }

    private static Map<Integer, Integer> countsOfElement(int[] arr) {
        /*
        Напишите метод, который получает на вход массив элементов и возвращает Map ключи в котором - элементы,
        а значения - сколько раз встретился этот элемент
         */
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            if (map.get(arr[i]) != null) {
                continue;
            }
            int count = 0;
            int item = arr[i];
            for (int j = i; j < arr.length; j++) {
                if (item == arr[j]) {
                    count++;
                }
            }
            map.put(item, count);
        }
        return map;
    }

    private static void blockingQ() {
        /*
        Предположим, у вас есть пул потоков, и вы хотите реализовать блокирующую очередь для передачи задач между потоками.
        Создайте класс BlockingQueue, который будет обеспечивать безопасное добавление и извлечение элементов между
        производителями и потребителями в контексте пула потоков.
        Класс BlockingQueue должен содержать методы enqueue() для добавления элемента в очередь и dequeue() для извлечения элемента.
         Если очередь пуста, dequeue() должен блокировать вызывающий поток до появления нового элемента.
        Очередь должна иметь фиксированный размер.
        Используйте механизмы wait() и notify() для координации между производителями и потребителями. Реализуйте метод size(),
         который возвращает текущий размер очереди.
         */
        BlockingQueue queue = new BlockingQueue();
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        Thread producerT = new Thread(producer);
        producerT.setName("producer");
        producerT.start();
        Thread consumerT = new Thread(consumer);
        consumerT.setName("consumer");
        consumerT.start();

    }

    private static void deadlock() {
        /*
        Изучите следующий код, найдите в нем потенциальные проблемы и исправьте их
         */
        DeadlockExample example = new DeadlockExample();
        example.execute();
    }


}
