public class ResultadoBusqueda {
    private boolean encontrado;
    private int posicion;
    private String nombre;
    private int stock;
    private double precio;

    public ResultadoBusqueda(boolean encontrado, int posicion, String nombre, int stock, double precio) {
        this.encontrado = encontrado;
        this.posicion = posicion;
        this.nombre = nombre;
        this.stock = stock;
        this.precio = precio;
    }

    public boolean isEncontrado() {
        return encontrado;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setEncontrado(boolean encontrado) {
        this.encontrado = encontrado;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "ResultadoBusqueda{" +
                "encontrado=" + encontrado +
                ", posicion=" + posicion +
                '}';
    }
}
