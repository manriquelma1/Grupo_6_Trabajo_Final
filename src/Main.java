import javax.naming.LimitExceededException;
import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    //Creacion de Listas para el inventario
    static LinkedList<Integer> codigo_producto= new LinkedList<Integer>();
    static LinkedList<String> productos= new LinkedList<String>();
    static LinkedList<Integer> stock= new LinkedList<Integer>();
    static LinkedList<Double> precios= new LinkedList<Double>();

    //Creacion de Lista para guardar el registro de ventas concretadas
    static LinkedList<Registro_pedido> bd_pedidos =new LinkedList<>();


    //Creacion de colas para los pedidos
    static LinkedList<Registro_pedido> cola_pedidos = new LinkedList<>();


    public static void main(String[] args) {
        crea_inventar();
        Scanner scanner = new Scanner(System.in);

        //Ordenamiento
        Ordenamiento_busqueda.ordenamiento_inventario(codigo_producto,productos,stock,precios);




        int opcion;
        System.out.println(codigo_producto);
        System.out.println(productos);
        System.out.println(stock);
        System.out.println(precios);
        do {
            System.out.println("\nMenú de Inventario:");
            System.out.println("1. Registrar producto");
            System.out.println("2. Atender Pedidos");  //Se atiende mediante colas
            System.out.println("3. Buscar producto por nombre");
            System.out.println("4. Mostrar inventario");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            System.out.println(codigo_producto);
            System.out.println(productos);
            System.out.println(stock);
            System.out.println(precios);


            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    canastacliente(cola_pedidos);
                    continue;
                case 2:
                    atender_pedido(cola_pedidos);
                    continue;
                case 3:

                default:
                    System.out.println("Opción no válida.");

            }

    } while (opcion != 5);

    scanner.close();


    }




    public static LinkedList<Registro_pedido> canastacliente(LinkedList<Registro_pedido> cola_pedidos) {
        Scanner scanner = new Scanner(System.in);
        int orden_compra = 0;

        if (cola_pedidos.isEmpty()){
            orden_compra = 1;
        }else{
            orden_compra=cola_pedidos.size()+1;
        }




        LinkedList<Integer> canasta_cliente = new LinkedList<>(); // Lista de productos del cliente
        LinkedList<Integer> unidades = new LinkedList<>();  // Lista de unidades por producto
        LinkedList<Double> precios_canasta = new LinkedList<>();  //Lista de precio por unidad
        LinkedList<String> productos_canasta = new LinkedList<>(); //Lista con los nombres de los productos


        System.out.println("Ingrese DNI cliente: ");
        String dni = scanner.nextLine();
        String opcion;
        do {
            System.out.println("Ingrese el codigo del producto: ");
            int producto = scanner.nextInt();
            ResultadoBusqueda resultado_busqueda = Ordenamiento_busqueda.busqueda_codigo(codigo_producto,producto,productos,stock,precios);
            if (resultado_busqueda.isEncontrado()) {
                System.out.println("Se cuenta con: "+resultado_busqueda.getStock()+" en stock");
                System.out.println("Ingrese la cantidad: ");
                int unidad = scanner.nextInt();
                if (unidad <= resultado_busqueda.getStock()){
                    unidades.add(unidad);
                    productos_canasta.add(resultado_busqueda.getNombre());
                    canasta_cliente.add(producto);
                    precios_canasta.add(resultado_busqueda.getPrecio());


                    //Actualizar el stock
                    Ordenamiento_busqueda.actualizar_stock(stock,resultado_busqueda.getPosicion(),"-");
                }else {
                    System.out.println("No se cuenta con el stock indicado");
                }

            }else {
                System.out.println("El producto no existe.");
            }
            scanner.nextLine();
            System.out.println("Desea agregar mas productos?:  ");
            opcion = scanner.nextLine();
        }while (opcion.equals("si"));

        cola_pedidos.add(new Registro_pedido(dni, canasta_cliente,orden_compra,unidades,precios_canasta,productos_canasta));

        return cola_pedidos;


    }

    public static void atender_pedido(LinkedList<Registro_pedido> cola_pedidos) {
        Scanner scanner = new Scanner(System.in);

        int opcion;
        if (cola_pedidos.isEmpty()) {
            System.out.println("No hay productos encontrados");
        } else {

            do {
                System.out.println(cola_pedidos.peek());
                System.out.println("Opcion 1:Atendido | Opcion 2: Aplazar |  Opcion 3:Cancelar Pedido");
                opcion = scanner.nextInt();
                scanner.nextLine();
                switch (opcion) {

                    case 1:
                        bd_pedidos.add(cola_pedidos.peek());
                        cola_pedidos.remove();
                    case 2:
                        Registro_pedido canasta_actual = cola_pedidos.peek();
                        cola_pedidos.remove();
                        cola_pedidos.add(canasta_actual);
                    case 3:
                        Registro_pedido pedido_actual = cola_pedidos.peek();
                        Ordenamiento_busqueda.actualizar_stock_masivo(pedido_actual,"+",codigo_producto,productos,stock,precios,0 );
                        cola_pedidos.remove();



                }
            } while (!cola_pedidos.isEmpty());
        }
    }

    public static void crea_inventar() {
        Random random = new Random();
        int[] codigo_inicio = {12, 13, 44, 4, 32, 21, 43, 14, 1, 10, 5, 35, 7, 22, 36, 26, 20, 3, 50, 30, 51, 11, 6, 33, 60};
        String[] productos_inicio = {"Arroz", "Frijoles", "Aceite", "Harina", "Azúcar", "Detergente", "Cloro", "Esponja", "Trapeador", "Jabón", "Agua", "Refresco", "Jugo", "Cerveza", "Vino", "Papas", "Galletas", "Chocolate", "Caramelos", "Fruta", "Toalla", "Vela", "Fósforos", "Papel higiénico", "Bolsa de basura"};
        double[] precios_inicio = {2.5, 3.0, 5.5, 2.3, 5.0, 6.5, 7.1, 2.3, 1.5, 8.2, 4.2, 6.5, 9.2, 4.6, 10.5, 11.2, 7.8, 5.6, 2.5, 4.0, 2.5, 3.5, 6.2, 14.2, 15.0};
        for (int i = 0; i < productos_inicio.length; i++) {
            productos.add(productos_inicio[i]);
            stock.add(random.nextInt(100) + 1);
            precios.add(precios_inicio[i]);
            codigo_producto.add(codigo_inicio[i]);
        }


    }
}