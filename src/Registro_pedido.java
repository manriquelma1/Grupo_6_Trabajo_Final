import java.util.LinkedList;

public class Registro_pedido {
    private String dni_cliente;
    private LinkedList<Integer> canasta;  //codigo del producto
    private int orden_compra;
    private LinkedList<Integer> unidades;
    private LinkedList<Double> precios;
    private LinkedList<String> nombre_producto;

    public Registro_pedido(String dni_cliente, LinkedList<Integer> canasta, int orden_compra, LinkedList<Integer> unidades, LinkedList<Double> precios,LinkedList<String> nombre_producto) {
        this.dni_cliente = dni_cliente;
        this.canasta = canasta;
        this.orden_compra = orden_compra;
        this.unidades=unidades;
        this.precios = precios;
        this.nombre_producto = nombre_producto;
    }

    public LinkedList<String> getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_prodcto(LinkedList<String> nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public LinkedList<Integer> getUnidades() {
        return unidades;
    }

    public void setUnidades(LinkedList<Integer> unidades) {
        this.unidades = unidades;
    }

    public LinkedList<Double> getPrecios() {
        return precios;
    }

    public void setPrecios(LinkedList<Double> precios) {
        this.precios = precios;
    }

    public String getDni_cliente() {
        return dni_cliente;
    }

    public void setDni_cliente(String dni_cliente) {
        this.dni_cliente = dni_cliente;
    }

    public LinkedList<Integer> getCanasta() {
        return canasta;
    }

    public void setCanasta(LinkedList<Integer> canasta) {
        this.canasta = canasta;
    }
    public int getOrden_compra() {
        return orden_compra;
    }
    public void setOrden_compra(int orden_compra) {
        this.orden_compra = orden_compra;
    }

    //int suma=0;
    public double total_compra(int indice) {     //FORMA RECURSIVA (1)
        if(indice == unidades.size()) {
            return 0;

        }
        else{
            double precio_parcial=precios.get(indice)*unidades.get(indice);
            return precio_parcial + total_compra(indice + 1);
        }

    }

    public String guia_productos(int indice){

        if(indice == unidades.size()) {
            return "";
        }
        else{
            String guia = String.format(
                    "%-5s %-15s %-5d %-10.2f %-100.2f",
                    canasta.get(indice),
                    nombre_producto.get(indice),
                    unidades.get(indice),
                    precios.get(indice),
                    precios.get(indice) * unidades.get(indice)
            );

            //String guia=canasta.get(indice)+" "+nombre_producto.get(indice)+" "+unidades.get(indice)+" "+precios.get(indice)*unidades.get(indice);
            return guia+"\n"+guia_productos(indice + 1);
        }

    }

    @Override
    public String toString() {
        String cliente_orden="Cliente: "+dni_cliente+"    Orden compra: "+orden_compra+"\n";
        String encabezado = String.format("%-5s %-15s %-5s %-10s %-10s\n", "ID", "Producto", "Cant","Precio","Total");
        encabezado += "------------------------------------------\n";

        String suma_total=String.format("%-38s %-100.2f","TOTAL:",total_compra(0));

        return cliente_orden + encabezado + guia_productos(0)+suma_total;
        //return  guia_productos(0);
        //return "Orden: "+orden_compra+"\nDNI del cliente: " + dni_cliente + "\nCanasta: " + canasta.toString() + "\ntotal a pagar"+total_compra( 0);
    }

}