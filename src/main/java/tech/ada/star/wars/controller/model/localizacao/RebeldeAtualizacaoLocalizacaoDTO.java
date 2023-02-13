package tech.ada.star.wars.controller.model.localizacao;

import javax.validation.constraints.NotBlank;

public class RebeldeAtualizacaoLocalizacaoDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
