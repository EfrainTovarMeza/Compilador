package DefinitiveEdition;

public class asignaciones {
	String nombre, tipo;

    public asignaciones(String nombre, String tipo) {
        this.tipo = tipo;
        this.nombre = nombre;      
    }


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
