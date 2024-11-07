package com.example;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

class Order {
    private final String product;  
    private final int quantity;    
    private final double price;    

    // Constructor para inicializar un pedido
    public Order(String product, int quantity, double price) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;

    }

    // Métodos getter para acceder a los valores del pedido
    public String getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}

public class EjemploEstudiante {
    public static void main(String[] args) {
        // Aqui esta es una lista de pedidos con diferentes productos, cantidades y precios
        List<Order> orders = Arrays.asList(
            new Order("Product A", 2, 50.0),
            new Order("Product B", 1, 30.0),
            new Order("Product A", 1, 50.0),
            new Order("Product C", 3, 20.0),
            new Order("Product A", 4, 50.0),
            new Order("Product B", 2, 30.0),
            new Order("Product C", 1, 20.0)
        );

        // Procesar las ventas para el Producto A
        processOrders(orders, "Product A")
            .subscribe(total -> System.out.println("Total sales for Product A: " + total));

        // Procesar las ventas para el Producto B
        processOrders(orders, "Product B")
            .subscribe(total -> System.out.println("Total sales for Product B: " + total));

        // Procesar las ventas para el Producto C
        processOrders(orders, "Product C")
            .subscribe(total -> System.out.println("Total sales for Product C: " + total));
    }

    // Método para procesar los pedidos, calcular las ventas totales para un producto específico
    public static Observable<Double> processOrders(List<Order> orders, String product) {
        return Observable.fromIterable(orders)
            .filter(order -> product.equals(order.getProduct()))
            // 'map' transforma cada pedido en el total de la venta (cantidad * precio)
            .map(order -> order.getQuantity() * order.getPrice())
            // 'reduce' acumula los resultados de las ventas para obtener el total
            .reduce(0.0, Double::sum) // Reduce un solo valor de tipo Double
            // 'toObservable()' convierte el resultado de 'reduce' en un Observable
            .toObservable()
            // Se usa un 'Scheduler' para mover la tarea de procesamiento a un hilo IO (entrada/salida)
            .subscribeOn(Schedulers.io())
            // un timeout de 5 segundos, para evitar que el proceso quede colgado indefinidamente
            .timeout(5, TimeUnit.SECONDS, Observable.just(0.0)); // Si se supera el tiempo, se retorna 0.0
    }

    // Método para contar la cantidad de pedidos de un producto específico
    public static Observable<Long> countOrders(List<Order> orders, String product) {
        return Observable.fromIterable(orders)
            // 'filter' filtra los pedidos que coinciden con el producto
            .filter(order -> product.equals(order.getProduct()))
            // 'count' cuenta el número total de pedidos para el producto
            .count()
            // 'toObservable()' convierte el Single en un Observable
            .toObservable()
            //  un 'Scheduler' para mover la tarea de procesamiento a un hilo IO (entrada/salida)
            .subscribeOn(Schedulers.io())
           
            .timeout(5, TimeUnit.SECONDS, Observable.just(0L)) // Si se supera el tiempo, se retorna 0L
            // 'defaultIfEmpty' garantiza que si no se encuentra ningún pedido, se retorne un valor por defecto (0L)
            .defaultIfEmpty(0L);
    }

    // Método para obtener los detalles de todos los pedidos de un producto específico
    public static Observable<String> processOrderDetails(List<Order> orders, String product) {
        return Observable.fromIterable(orders)
            .filter(order -> product.equals(order.getProduct()))
            .map(order -> "Product: " + order.getProduct() +
                          ", Quantity: " + order.getQuantity() +
                          ", Price: " + order.getPrice())
            .toList()
            // convierte la lista en un Observable, emitiendo cada elemento individualmente
            .flatMapObservable(list -> list.isEmpty() 
                ? Observable.just("No orders found for " + product)  // Si no hay pedidos, emitimos un mensaje
                : Observable.fromIterable(list))  // Si hay pedidos, los emitimos
            // Scheduler para mover la tarea de procesamiento a un hilo IO (entrada/salida)
            .subscribeOn(Schedulers.io())
           
            .timeout(5, TimeUnit.SECONDS); 
    }
}

//En este codigo utilizo varios operadores clave de RxJava para manejar los flujos de datos. Primero, aplico el operador filter para seleccionar solo los pedidos que coinciden con el producto que estoy procesando. Luego, utilizo el operador map para transformar cada pedido en el total de la venta, multiplicando la cantidad por el precio. Para obtener el total acumulado de las ventas, empleo el operador reduce, que va sumando los valores de las ventas. También uso el operador toList para agrupar los resultados en una lista y flatMapObservable para convertir esa lista en un flujo de elementos individuales, que luego puedo procesar. Además, con subscribeOn, muevo la ejecución al hilo IO para que se maneje de manera eficiente, y con timeout, aseguro que el proceso no se quede colgado demasiado tiempo, proporcionando un valor predeterminado si el tiempo se excede. Todos estos operadores me permiten gestionar de forma eficaz los flujos asíncronos y concurrentes mientras procesaba los pedidos.
