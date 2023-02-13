package tech.ada.star.wars.controller.model.recurso;

import tech.ada.star.wars.data.entity.Item;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class RecursoDTO {

    @NotNull(message = "Item de inventário é obrigatório")
    private Item item;

    @Min(value = 1L, message = "Deve existir pelo menos um item para o recurso")
    @NotNull(message = "Quantidade de item de inventário é obrigatório")
    private Long quantidade;

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

}
