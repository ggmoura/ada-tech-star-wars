package tech.ada.star.wars.controller.model.rebelde;

import tech.ada.star.wars.controller.model.localizacao.LocalizacaoDTO;
import tech.ada.star.wars.controller.model.localizacao.LocalizacaoRequestDTO;
import tech.ada.star.wars.controller.model.recurso.RecursoDTO;
import tech.ada.star.wars.controller.model.recurso.RecursoRequestDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class RebeldeRequestDTO extends RebeldeDTO {

    @NotNull(message = "Localização é obrigatório")
    private @Valid LocalizacaoRequestDTO localizacao;

    @NotEmpty(message = "Deve ser informado pelo menos um Recurso")
    private List<@Valid RecursoRequestDTO> inventario;

    public LocalizacaoRequestDTO getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(LocalizacaoRequestDTO localizacao) {
        this.localizacao = localizacao;
    }

    public List<RecursoRequestDTO> getInventario() {
        return inventario;
    }

    public void setInventario(List<RecursoRequestDTO> inventario) {
        this.inventario = inventario;
    }
}
