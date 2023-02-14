package tech.ada.star.wars.controller.relatorio;

public class RelatorioPontosPerdidosDTO {

    private String nomeRelatorio;
    private Long quantidadePontos;

    public String getNomeRelatorio() {
        return nomeRelatorio;
    }

    public void setNomeRelatorio(String nomeRelatorio) {
        this.nomeRelatorio = nomeRelatorio;
    }

    public Long getQuantidadePontos() {
        return quantidadePontos;
    }

    public void setQuantidadePontos(Long quantidadePontos) {
        this.quantidadePontos = quantidadePontos;
    }
}
