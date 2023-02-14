package tech.ada.star.wars.controller.relatorio;

public class RelatorioPorcentagemDTO {

    private String nomeRelatorio;
    private String porcentagem;
    private Long quantidadeTraidor;
    private Long quantidadeRebelde;

    public String getNomeRelatorio() {
        return nomeRelatorio;
    }

    public void setNomeRelatorio(String nomeRelatorio) {
        this.nomeRelatorio = nomeRelatorio;
    }

    public Long getQuantidadeTraidor() {
        return quantidadeTraidor;
    }

    public void setQuantidadeTraidor(Long quantidadeTraidor) {
        this.quantidadeTraidor = quantidadeTraidor;
    }

    public Long getQuantidadeRebelde() {
        return quantidadeRebelde;
    }

    public void setQuantidadeRebelde(Long quantidadeRebelde) {
        this.quantidadeRebelde = quantidadeRebelde;
    }

    public String getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(String porcentagem) {
        this.porcentagem = porcentagem;
    }
}
