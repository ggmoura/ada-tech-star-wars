package tech.ada.star.wars.controller.model.localizacao;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LocalizacaoDTO {

    @NotNull(message = "Latitude da localização é obrigatório")
    private Long latitude;

    @NotNull(message = "Longitude da localização é obrigatório")
    private Long longitude;

    @NotBlank(message = "Nome da Base da localização é obrigatório")
    private String nomeBase;

    public Long getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public Long getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    public String getNomeBase() {
        return nomeBase;
    }

    public void setNomeBase(String nomeBase) {
        this.nomeBase = nomeBase;
    }
}
