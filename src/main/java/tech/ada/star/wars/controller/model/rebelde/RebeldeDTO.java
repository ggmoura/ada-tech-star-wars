package tech.ada.star.wars.controller.model.rebelde;

import tech.ada.star.wars.data.entity.Genero;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public class RebeldeDTO {

    @NotBlank(message = "Nome do Rebelde é obrigatório")
    private String nome;

    @NotNull(message = "Idade do Rebelde é obrigatório")
    private Integer idade;

    @NotNull(message = "Genero do Rebelde é obrigatório")
    private Genero genero;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

}
