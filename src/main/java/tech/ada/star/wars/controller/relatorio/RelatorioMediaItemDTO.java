package tech.ada.star.wars.controller.relatorio;

import tech.ada.star.wars.data.entity.Item;

import java.util.Map;

public class RelatorioMediaItemDTO {

    private String nomeRelatorio;
    private Map<Item, String> mediaRecursos;

    public Map<Item, String> getMediaRecursos() {
        return mediaRecursos;
    }

    public void setMediaRecursos(Map<Item, String> mediaRecursos) {
        this.mediaRecursos = mediaRecursos;
    }

    public String getNomeRelatorio() {
        return nomeRelatorio;
    }

    public void setNomeRelatorio(String nomeRelatorio) {
        this.nomeRelatorio = nomeRelatorio;
    }
}
