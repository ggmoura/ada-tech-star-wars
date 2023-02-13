package tech.ada.star.wars.controller.model.localizacao;

import javax.validation.constraints.NotNull;

public class LocalizacaoAtualizacaoRequestDTO extends LocalizacaoDTO {

    @NotNull(message = "O rebelde é obrigatório")
    private RebeldeAtualizacaoLocalizacaoDTO rebelde;

    public RebeldeAtualizacaoLocalizacaoDTO getRebelde() {
        return rebelde;
    }

    public void setRebelde(RebeldeAtualizacaoLocalizacaoDTO rebelde) {
        this.rebelde = rebelde;
    }

}
