package DefinitiveEdition;

public class idsval {
    String token,tipo,nombre,valor,idval;

    public idsval(String token, String tipo, String nombre, String valor, String idval) {
        this.token = token;
        this.tipo = tipo;
        this.nombre = nombre;
        this.valor = valor;
        this.idval = idval;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getIdval() {
        return idval;
    }

    public void setIdval(String idval) {
        this.idval = idval;
    }
    
}
