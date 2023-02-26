package tech.ada.star.wars.controller.relatorio;

import com.fasterxml.jackson.annotation.JsonProperty;
import tech.ada.star.wars.data.entity.Genero;


public class RebeldeListResponseDTO {

    private String nome;
    private Integer idade;
    private Genero genero;
    @JsonProperty("total_pontos")
    private Long totalPontos;

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

    public Long getTotalPontos() {
        return totalPontos;
    }

    public void setTotalPontos(Long totalPontos) {
        this.totalPontos = totalPontos;
    }
}
