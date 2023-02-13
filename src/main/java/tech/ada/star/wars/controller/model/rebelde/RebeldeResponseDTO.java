package tech.ada.star.wars.controller.model.rebelde;

import tech.ada.star.wars.controller.model.localizacao.LocalizacaoResponseDTO;
import tech.ada.star.wars.controller.model.recurso.RecursoResponseDTO;

import java.util.List;

public class RebeldeResponseDTO extends RebeldeDTO {

    private LocalizacaoResponseDTO localizacao;
    private List<RecursoResponseDTO> inventario;

    public LocalizacaoResponseDTO getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(LocalizacaoResponseDTO localizacao) {
        this.localizacao = localizacao;
    }

    public List<RecursoResponseDTO> getInventario() {
        return inventario;
    }

    public void setInventario(List<RecursoResponseDTO> inventario) {
        this.inventario = inventario;
    }

}
