package tech.ada.star.wars.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Localizacao extends BaseEntity<Long> {

    @NotNull
    @Column(name = "latitude")
    private Long latitude;

    @NotNull
    @Column(name = "longitude")
    private Long longitude;

    @NotNull
    @Column(name = "nome_base")
    private String nomeBase;

    @OneToOne(mappedBy = "localizacao")
    private Rebelde rebelde;

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

    public Rebelde getRebelde() {
        return rebelde;
    }

    public void setRebelde(Rebelde rebelde) {
        this.rebelde = rebelde;
    }
}
