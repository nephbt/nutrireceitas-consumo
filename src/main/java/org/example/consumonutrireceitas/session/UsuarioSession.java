package org.example.consumonutrireceitas.session;

import org.example.consumonutrireceitas.model.Usuario;

public class UsuarioSession {
    private static UsuarioSession instance;
    private Usuario usuario;

    private UsuarioSession() {}

    public static UsuarioSession getInstance() {
        if (instance == null) {
            instance = new UsuarioSession();
        }
        return instance;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public  void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
