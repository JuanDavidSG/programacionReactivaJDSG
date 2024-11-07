package com.example;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class EjemploEstudianteTest  {

    private List<Order> orders;

    @BeforeEach
    void setUp() {
        orders = Arrays.asList(
            new Order("Product A", 2, 50.0),
            new Order("Product B", 1, 30.0),
            new Order("Product A", 1, 50.0),
            new Order("Product C", 3, 20.0),
            new Order("Product A", 4, 50.0),
            new Order("Product B", 2, 30.0),
            new Order("Product C", 1, 20.0)
        );
    }

    @Test
    void testProcessOrdersForProductA() {
        TestObserver<Double> testObserver = new TestObserver<>();
        Observable<Double> result = EjemploEstudiante.processOrders(orders, "Product A");
        
        result.subscribe(testObserver);
        
        testObserver.awaitTerminalEvent();
        testObserver.assertValue(350.0); // 2*50 + 1*50 + 4*50 = 350
    }

    @Test
    void testProcessOrdersForProductB() {
        TestObserver<Double> testObserver = new TestObserver<>();
        Observable<Double> result = EjemploEstudiante.processOrders(orders, "Product B");
        
        result.subscribe(testObserver);
        
        testObserver.awaitTerminalEvent();
        testObserver.assertValue(90.0); // 1*30 + 2*30 = 90
    }

    @Test
    void testProcessOrdersForProductC() {
        TestObserver<Double> testObserver = new TestObserver<>();
        Observable<Double> result = EjemploEstudiante.processOrders(orders, "Product C");
        
        result.subscribe(testObserver);
        
        testObserver.awaitTerminalEvent();
        testObserver.assertValue(80.0); // 3*20 + 1*20 = 80
    }

    @Test
    void testProcessOrdersForNonExistentProduct() {
        TestObserver<Double> testObserver = new TestObserver<>();
        Observable<Double> result = EjemploEstudiante.processOrders(orders, "Product X");
        
        result.subscribe(testObserver);
        
        testObserver.awaitTerminalEvent();
        testObserver.assertValue(0.0); // No orders for Product X, expected total = 0.0
    }

    @Test
    void testProcessOrdersForEmptyList() {
        TestObserver<Double> testObserver = new TestObserver<>();
        Observable<Double> result = EjemploEstudiante.processOrders(Collections.emptyList(), "Product A");
        
        result.subscribe(testObserver);
        
        testObserver.awaitTerminalEvent();
        testObserver.assertValue(0.0); // Empty list should return total = 0.0
    }
    
}

