package tech.ada.star.wars.controller.model.negociacao;

import tech.ada.star.wars.controller.model.recurso.RecursoDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class RebeldeNegociadorDTO {

    @NotBlank(message = "O nome do Rebelde é obrigatório")
    private String nome;

    @NotEmpty(message = "Informe os itens a serem negociados")
    private List<@Valid RecursoDTO> recusrsosOferecidos;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<RecursoDTO> getRecusrsosOferecidos() {
        return recusrsosOferecidos;
    }

    public void setRecusrsosOferecidos(List<RecursoDTO> recusrsosOferecidos) {
        this.recusrsosOferecidos = recusrsosOferecidos;
    }
}
