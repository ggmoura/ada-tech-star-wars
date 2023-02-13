package tech.ada.star.wars.data.entity;

import javax.persistence.*;

@Entity
@Table(name = "RECURSO", indexes = {
        @Index(name = "recurso_rebelde_item", unique = true, columnList = "item, id_rebelde")
})
public class Recurso extends BaseEntity<Long> {

    public Recurso() {
        super();
    }

    public Recurso(Item item, Long quantidade) {
        this();
        this.item = item;
        this.quantidade = quantidade;
    }

    @Enumerated(EnumType.STRING)
    private Item item;
    private Long quantidade;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_rebelde")
    private Rebelde rebelde;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }

    public Rebelde getRebelde() {
        return rebelde;
    }

    public void setRebelde(Rebelde rebelde) {
        this.rebelde = rebelde;
    }
}
