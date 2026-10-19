package com.example.chamados.dto;

import com.example.chamados.entity.Perfil;
import jakarta.validation.constraints.NotNull;

public class CadastroUsuarioForm extends CadastroPublicoForm {

    @NotNull(message = "Selecione um perfil.")
    private Perfil perfil;

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}
