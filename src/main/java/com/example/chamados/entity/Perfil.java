package com.example.chamados.entity;

public enum Perfil {
    ADMINISTRADOR,
    USUARIO;

    public String autoridade() {
        return "ROLE_" + name();
    }
}
