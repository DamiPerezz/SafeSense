package Model;

public class ListaEnlazada<T> {
    private Nodo<T> primero;

    public ListaEnlazada() {
        this.primero = null;
    }

    public void insertar(T elemento) {
        Nodo<T> nuevo = new Nodo<>(elemento);
        nuevo.setEnlace(primero);
        primero = nuevo;
    }

    public Nodo<T> getPrimero() {
        return primero;
    }
}
