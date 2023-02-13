package tech.ada.star.wars.controller.model.rebelde;

import javax.validation.constraints.NotEmpty;

public class RebeldeTraidorRequestDTO {

    @NotEmpty(message = "O nome do rebelde é obrigatório")
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
