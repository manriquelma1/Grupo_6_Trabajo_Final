import java.util.*;

public class Ordenamiento_busqueda {
    public static void intercambio(LinkedList<Integer> lista, int i, int j) {
        int temporal = lista.get(i);
        lista.set(i, lista.get(j));
        lista.set(j, temporal);

    }

    public static void intercambio_string(LinkedList<String> lista, int i, int j) {
        String temporal = lista.get(i);
        lista.set(i, lista.get(j));
        lista.set(j, temporal);
    }

    public static void intercambio_double(LinkedList<Double> lista, int i, int j) {
        double temporal = lista.get(i);
        lista.set(i, lista.get(j));
        lista.set(j, temporal);
    }


    public static void ordenamiento_inventario(LinkedList<Integer> codigo_pro, LinkedList<String> productos_arr, LinkedList<Integer> stock_arr, LinkedList<Double> precios_arr) {

        int i = 0;
        int j = 0;
        for (i = 0; i < codigo_pro.size() - 1; i++) {
            for (j = 0; j < codigo_pro.size() - 1; j++) {
                if (codigo_pro.get(j + 1) < codigo_pro.get(j)) {
                    intercambio(codigo_pro, j, j + 1);
                    intercambio_string(productos_arr, j, j + 1);
                    intercambio(stock_arr, j, j + 1);
                    intercambio_double(precios_arr, j, j + 1);
                }
            }
        }
    }

    public static ResultadoBusqueda busqueda_codigo(LinkedList<Integer> codigo_pro, int buscar, LinkedList<String> productos_arr, LinkedList<Integer> stock_arr, LinkedList<Double> precios_arr) {

        boolean estado = false;
        int posicion_inicial = 1;
        int posicion_final = codigo_pro.size() - 1;
        int cont = 0;
        int valor_buscar = buscar;
        while (posicion_inicial <= posicion_final) {

            int posicion_medio = (int) Math.floor((posicion_inicial + posicion_final) / 2);
            int elemento = codigo_pro.get(posicion_medio);

            if (elemento == valor_buscar) { //cadena.equals(valor_buscar)

                posicion_final = 0;
                posicion_inicial = 1;
                return new ResultadoBusqueda(true, posicion_medio, productos_arr.get(posicion_medio), stock_arr.get(posicion_medio), precios_arr.get(posicion_medio));
            } else {
                if (elemento < valor_buscar) //elemento.compareTo(valor_buscar) > 0
                    posicion_inicial = posicion_medio + 1;
                else
                    posicion_final = posicion_medio - 1;
            }
            cont++;
        }
        return new ResultadoBusqueda(false, -1, "No encontrado", 0, 0);


    }


    public static void actualizar_stock(LinkedList<Integer> stock, int posicion, String accion) {
        if (accion.equals("-")) {
            stock.set(posicion, stock.get(posicion) - 1);
        } else if (accion.equals("+")) {
            stock.set(posicion, stock.get(posicion) + 1);
        }
    }

    public static void actualizar_stock_inv(LinkedList<Integer> stock, int posicion, String accion, int agregar_unidades) {
        if (accion.equals("-")) {
            stock.set(posicion, stock.get(posicion) - agregar_unidades);
        } else if (accion.equals("+")) {
            stock.set(posicion, stock.get(posicion) + agregar_unidades);
        }
    }


    //RECURSIVIDAD
    public static void actualizar_stock_masivo(Registro_pedido canasta, String accion, LinkedList<Integer> codigo_pro, LinkedList<String> productos_arr, LinkedList<Integer> stock_arr, LinkedList<Double> precios_arr, int posicion) {

        if (posicion == canasta.getCanasta().size()) {
            return;
        } else {
            LinkedList<Integer> codigos = canasta.getCanasta();

            ResultadoBusqueda resultadoBusqueda = busqueda_codigo(codigo_pro, codigos.get(posicion), productos_arr, stock_arr, precios_arr);

            actualizar_stock_inv(stock_arr, resultadoBusqueda.getPosicion(), "+", canasta.getUnidades().get(posicion));
        }
        actualizar_stock_masivo(canasta, accion, codigo_pro, productos_arr, stock_arr, precios_arr, posicion + 1);
    }

    //Busqueda Secuencial no ordenada de productos dentro de una lista
    public static void busqueda_producto(LinkedList<Integer> codigo_producto, LinkedList<String> productos, LinkedList<Integer> stock, LinkedList<Double> precios) {
        // Convertir LinkedLists a ArrayLists
        ArrayList<Integer> codigoArr = new ArrayList<>(codigo_producto);
        ArrayList<String> productosArr = new ArrayList<>(productos);
        ArrayList<Integer> stockArr = new ArrayList<>(stock);
        ArrayList<Double> preciosArr = new ArrayList<>(precios);


        Scanner scanner = new Scanner(System.in);

        //Opciones que debe elegir el usuario

        System.out.println("1. Buscar reporte del producto por nombre");
        System.out.print("2. Generar reporte de todos los productos en stock\n");
        System.out.println("3. Regresar al menu principal");
        System.out.println("\nSeleccione una opción: ");
        int opcion = scanner.nextInt();
        if (opcion == 1) {
            //Busqueda de producto por nombre
            System.out.println("Ingrese el nombre del producto: ");
            String nombreProducto = scanner.next();
            int productIndex = -1;
            for (int i = 0; i < productos.size(); i++) {
                if (productos.get(i).equalsIgnoreCase(nombreProducto)) {
                    productIndex = i;
                    break;
                }
            }
            // If product is found, show details, otherwise print a message
            if (productIndex != -1) {
                System.out.println("Producto encontrado:");
                System.out.println("Código: " + codigo_producto.get(productIndex));
                System.out.println("Nombre: " + productos.get(productIndex));
                System.out.println("Stock disponible: " + stock.get(productIndex));
                System.out.println("Precio: " + precios.get(productIndex));
            } else {
                System.out.println("Producto no encontrado.");
            }


        } else if (opcion == 2) {
            //Mostrar todos los productos ordenados por stock de menor a mayor
            for (int i = 1; i < stockArr.size(); i++) {
                int arrstock = stockArr.get(i);
                int arrcodigo = codigoArr.get(i);
                String arrproducoto = productosArr.get(i);
                double arrprecio = preciosArr.get(i);

                int j = i - 1;
                while (j >= 0 && stockArr.get(j) > arrstock) {
                    stockArr.set(j + 1, stockArr.get(j));
                    codigoArr.set(j + 1, codigoArr.get(j));
                    productosArr.set(j + 1, productosArr.get(j));
                    preciosArr.set(j + 1, preciosArr.get(j));
                    j = j - 1;
                }
                stockArr.set(j + 1, arrstock);
                codigoArr.set(j + 1, arrcodigo);
                productosArr.set(j + 1, arrproducoto);
                preciosArr.set(j + 1, arrprecio);
            }
            System.out.println("Listado de productos ordenados por stock (de menor a mayor):");
            for (int i = 0; i < productosArr.size(); i++) {
                System.out.println("----------");
                System.out.println("Código: " + codigoArr.get(i));
                System.out.println("Nombre: " + productosArr.get(i));
                System.out.println("Stock disponible: " + stockArr.get(i));
                System.out.println("Precio: " + preciosArr.get(i));
                System.out.println("----------");
            }
        } else if (opcion == 3) {
            return;
        } else {
            System.out.println("Producto ingresado es invalido");
        }

    }
}









