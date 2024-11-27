import java.util.LinkedList;

public class Ordenamiento_busqueda {
    public static void intercambio(LinkedList<Integer> lista, int i, int j){
        int temporal = lista.get(i);
        lista.set(i, lista.get(j));
        lista.set(j, temporal);

    }

    public static void intercambio_string(LinkedList<String> lista, int i, int j){
        String temporal = lista.get(i);
        lista.set(i, lista.get(j));
        lista.set(j, temporal);
    }
    public static void intercambio_double(LinkedList<Double> lista, int i, int j){
        double temporal = lista.get(i);
        lista.set(i, lista.get(j));
        lista.set(j, temporal);
    }




    public static void ordenamiento_inventario(LinkedList<Integer> codigo_pro, LinkedList<String> productos_arr, LinkedList<Integer> stock_arr, LinkedList<Double> precios_arr){

        int i = 0;
        int j = 0;
        for (i=0; i< codigo_pro.size()-1; i++){
            for (j=0; j<codigo_pro.size()-1; j++){
                if (codigo_pro.get(j+1) < codigo_pro.get(j)){
                    intercambio(codigo_pro, j, j+1);
                    intercambio_string(productos_arr, j, j+1);
                    intercambio(stock_arr, j, j+1);
                    intercambio_double(precios_arr, j, j+1);
                }
            }
        }
    }

    public static ResultadoBusqueda busqueda_codigo(LinkedList<Integer> codigo_pro, int buscar,LinkedList<String> productos_arr, LinkedList<Integer> stock_arr, LinkedList<Double> precios_arr){

        boolean estado = false;
        int posicion_inicial = 1;
        int posicion_final = codigo_pro.size()-1;
        int cont = 0;
        int valor_buscar = buscar;
        while (posicion_inicial <= posicion_final){

            int posicion_medio = (int)Math.floor((posicion_inicial + posicion_final)/2);
            int elemento = codigo_pro.get(posicion_medio);
            System.out.println("posicion:" + elemento);
            if (elemento == valor_buscar) { //cadena.equals(valor_buscar)
                System.out.println("Encontre el valor");
                posicion_final = 0; posicion_inicial = 1;
                return new ResultadoBusqueda(true,posicion_medio,productos_arr.get(posicion_medio),stock_arr.get(posicion_medio),precios_arr.get(posicion_medio));
            }
            else{
                if (elemento < valor_buscar) //elemento.compareTo(valor_buscar) > 0
                    posicion_inicial = posicion_medio + 1;
                else
                    posicion_final = posicion_medio - 1;
            }
            cont++;
        }
        return new ResultadoBusqueda(false,-1,"No encontrado",0,0);


    }


    public static void  actualizar_stock(LinkedList<Integer> stock,int posicion,String accion){
        if (accion.equals("-")) {
            stock.set(posicion, stock.get(posicion) - 1);
        }else if (accion.equals("+")) {
            stock.set(posicion, stock.get(posicion) + 1);
        }
    }


    //RECURSIVIDAD
    public static void actualizar_stock_masivo(Registro_pedido canasta, String accion,LinkedList<Integer> codigo_pro,LinkedList<String> productos_arr, LinkedList<Integer> stock_arr, LinkedList<Double> precios_arr,int posicion){

        if(posicion == canasta.getCanasta().size()-1) {
            return;
        }else {
            LinkedList<Integer> codigos = canasta.getCanasta();

            ResultadoBusqueda resultadoBusqueda = busqueda_codigo(codigo_pro, codigos.get(posicion), productos_arr, stock_arr, precios_arr);

            actualizar_stock(stock_arr, resultadoBusqueda.getPosicion(), "+");
        }
        actualizar_stock_masivo(canasta, accion,codigo_pro,productos_arr,stock_arr,precios_arr,posicion +1);
    }

}






