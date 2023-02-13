package tech.ada.star.wars.controller.model.negociacao;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class NegociacaoDTO {

    @NotNull(message = "O negociador Fonte deve ser informado")
    private @Valid RebeldeNegociadorDTO negociadorFonte;

    @NotNull(message = "O negociador Alvo deve ser informado")
    private @Valid RebeldeNegociadorDTO negociadorAlvo;

    public RebeldeNegociadorDTO getNegociadorFonte() {
        return negociadorFonte;
    }

    public void setNegociadorFonte(RebeldeNegociadorDTO negociadorFonte) {
        this.negociadorFonte = negociadorFonte;
    }

    public RebeldeNegociadorDTO getNegociadorAlvo() {
        return negociadorAlvo;
    }

    public void setNegociadorAlvo(RebeldeNegociadorDTO negociadorAlvo) {
        this.negociadorAlvo = negociadorAlvo;
    }
}
