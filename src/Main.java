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
        //cargamos el inventario con datos de ejemplo
        crea_inventar();
        Scanner scanner = new Scanner(System.in);

        //Ordenamiento el inventario con referencia al codigo
        Ordenamiento_busqueda.ordenamiento_inventario(codigo_producto,productos,stock,precios);




        int opcion;
        System.out.println(codigo_producto);
        System.out.println(productos);
        System.out.println(stock);
        System.out.println(precios);

        //Menu de opciones
        do {
            System.out.println("\nMenú de Inventario:");
            System.out.println("1. Registrar producto");
            System.out.println("2. Atender Pedidos");  //Se atiende mediante colas
            System.out.println("3. Opciones de Inventario");
            System.out.println("4. Reportes");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            //capturamos la opcion
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    //Funcion para armar la canasta del cliente.
                    canastacliente(cola_pedidos);
                    continue;
                case 2:

                    //Funcion para atender los pedidos por cola
                    atender_pedido(cola_pedidos);
                    continue;
                case 3:
                    //Funcion para actualizar inventario y precios
                    actualizar_inventario();
                    continue;
                case 4:
                    //Funcion para ver el reporte de un solo producto o de todos los productos ordenado de menor a mayor stock
                    Ordenamiento_busqueda.busqueda_producto(codigo_producto, productos, stock, precios);
                default:
                    continue;
                    //System.out.println("Opción no válida.");

            }

    } while (opcion != 5);

    scanner.close();


    }



    //Metodo para armar la canasta del cliente 
    public static LinkedList<Registro_pedido> canastacliente(LinkedList<Registro_pedido> cola_pedidos) {
        Scanner scanner = new Scanner(System.in);
        int orden_compra = 0;

        if (cola_pedidos.isEmpty()){

            //Si la cola de pedidos esta vacia comenzamos brindando el orden de compra desde el 1
            orden_compra = 1;
        }else{

            //Sino sumamos 1 para el nuevo orden de compra
            orden_compra=cola_pedidos.size()+1;
        }



        //generamos listas para la clase Registro_pedido
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

            //Se genera un objeto para la busqueda del codigo
            ResultadoBusqueda resultado_busqueda = Ordenamiento_busqueda.busqueda_codigo(codigo_producto,producto,productos,stock,precios);


            if (resultado_busqueda.isEncontrado()) {  //Si se encuentra el codigo
                System.out.println("Se cuenta con: "+resultado_busqueda.getStock()+" en stock");
                System.out.println("Ingrese la cantidad: ");
                int unidad = scanner.nextInt();

                //La unidad ingresada tiene que ser menor o igual al stock del producto
                if (unidad <= resultado_busqueda.getStock()){

                    //agregagamos a la lista unidades
                    unidades.add(unidad);

                    //agregamos el nombre del producto a la lista
                    productos_canasta.add(resultado_busqueda.getNombre());

                    //agregamos la cantidad que se comprara
                    canasta_cliente.add(producto);
                    precios_canasta.add(resultado_busqueda.getPrecio());


                    //Actualizamos el stock en la lista principal
                    Ordenamiento_busqueda.actualizar_stock_inv(stock,resultado_busqueda.getPosicion(),"-",unidad);


                //Si el valor supera al del stock se retorna mensaje
                }else {
                    System.out.println("No se cuenta con el stock indicado");
                }

            // Si no se encuentra el producto se retorna mensaje
            }else {
                System.out.println("El producto no existe.");
            }

            scanner.nextLine();
            System.out.println("Desea agregar mas productos?:  ");
            opcion = scanner.nextLine();
        }while (opcion.equals("si"));


        //Cuando se termine de escoger los productos se guardan en la cola
        cola_pedidos.add(new Registro_pedido(dni, canasta_cliente,orden_compra,unidades,precios_canasta,productos_canasta));

        return cola_pedidos;


    }



    //Metodo para la atencion por cola
    public static void atender_pedido(LinkedList<Registro_pedido> cola_pedidos) {
        Scanner scanner = new Scanner(System.in);

        int opcion;

        //Si la cola esta vacia retornamos mensaje
        if (cola_pedidos.isEmpty()) {
            System.out.println("No hay productos encontrados");
        } else {

            do {

                //Imprimimos un resumen del pedido, cola_pedidos.peek retorna un objeto de la clase Registro_pedido
                System.out.println(cola_pedidos.peek());

                //Damos las opciones
                System.out.println("Opcion 1:Atendido | Opcion 2: Aplazar |  Opcion 3:Cancelar Pedido");
                opcion = scanner.nextInt();
                scanner.nextLine();
                switch (opcion) {

                    case 1:
                        //Para la opcion 1 guardamos el obejto en la lista de pedidos concretado
                        bd_pedidos.add(cola_pedidos.peek());

                        //Luego eliminamos la orden atendida de la cola
                        cola_pedidos.remove();
                    case 2:

                        //Para la opcion 2, guardamos el obejeto al final de la cola y la eliminamos de la posicion inicial
                        Registro_pedido canasta_actual = cola_pedidos.peek();
                        cola_pedidos.remove();
                        cola_pedidos.add(canasta_actual);


                    case 3:
                        //Para la opcion 3, cancelamos el pedido
                        Registro_pedido pedido_actual = cola_pedidos.peek();
                        Ordenamiento_busqueda.actualizar_stock_masivo(pedido_actual,"+",codigo_producto,productos,stock,precios,0 );
                        cola_pedidos.remove();



                }
            } while (!cola_pedidos.isEmpty());
        }
    }

    public static void  actualizar_inventario(){
        Scanner scanner = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("Actualizacion de Inventario");
            System.out.println("Opcion 1: Ingresar producto");
            System.out.println("Opcion 2: Ajustar Precio");
            System.out.println("Opcion 3: Buscar Producto");
            System.out.println("Opcion 4: Volver");
            System.out.println("Opcion 5: Salir");
            opcion = scanner.nextInt();
            scanner.nextLine();
            int producto_agregar;
            switch (opcion){
                case 1:
                    System.out.print("Ingrese el codigo del producto: ");
                    producto_agregar = scanner.nextInt();
                    scanner.nextLine();

                    ResultadoBusqueda busqueda_codigo_producto = Ordenamiento_busqueda.busqueda_codigo(codigo_producto,producto_agregar,productos,stock,precios);
                    if (busqueda_codigo_producto.isEncontrado()) {
                        System.out.println("El codigo ya existe en el inventario, se cuenta con "+busqueda_codigo_producto.getStock()+" en stock");
                        System.out.print("Unidades a agregar: ");
                        int unidades = scanner.nextInt();
                        scanner.nextLine();
                        Ordenamiento_busqueda.actualizar_stock_inv(stock, busqueda_codigo_producto.getPosicion(), "+",unidades);
                        int nuevo_stock=busqueda_codigo_producto.getStock()+unidades;
                        System.out.println("Ahora el producto "+busqueda_codigo_producto.getNombre()+" cuenta con "+nuevo_stock+" en stock");

                    }else {
                        System.out.print("Agregue el nombre del nuevo producto: ");
                        String nombre = scanner.nextLine();

                        System.out.print("Agregue las unidades a ingresar: ");
                        int unidades = scanner.nextInt();
                        scanner.nextLine();

                        System.out.println("Ingrese el precio por unidad para el  producto: ");
                        double precio = scanner.nextDouble();
                        scanner.nextLine();

                        codigo_producto.add(producto_agregar);
                        productos.add(nombre);
                        stock.add(unidades);
                        precios.add(precio);

                        //Ordenamiento
                        Ordenamiento_busqueda.ordenamiento_inventario(codigo_producto,productos,stock,precios);


                    }
                    continue;
                case 2:
                    System.out.print("Ingrese el codigo del producto: ");
                    producto_agregar = scanner.nextInt();
                    scanner.nextLine();
                    ResultadoBusqueda busqueda_codigo_producto_ss = Ordenamiento_busqueda.busqueda_codigo(codigo_producto,producto_agregar,productos,stock,precios);
                    if (!busqueda_codigo_producto_ss.isEncontrado()) {
                        System.out.print("Codigo no econtrado, vuelva al menu para registrar el codigo");

                    }else {
                        System.out.print("Ingrese el nuevo precio para "+busqueda_codigo_producto_ss.getNombre() + " ( "+producto_agregar+" )");
                        double ss=scanner.nextDouble();
                        scanner.nextLine();
                        precios.set(busqueda_codigo_producto_ss.getPosicion(),ss);
                    }
                    continue;
                case 3:
                    System.out.print("Ingrese el codigo del producto: ");
                    int buscar_producto = scanner.nextInt();
                    scanner.nextLine();

                    ResultadoBusqueda resultado_busqueda=Ordenamiento_busqueda.busqueda_codigo(codigo_producto,buscar_producto,productos,stock,precios);
                    if (resultado_busqueda.isEncontrado()) {
                        System.out.println(resultado_busqueda);
                    }else{
                        System.out.print("No se encontro el codigo en el inventario");
                    }
                    continue;
                case 5:
                    break;
            }
        }while (opcion != 4);

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