package tech.ada.star.wars.data.entity;

public enum Item {

    ARMA("Arma", 4),
    MUNICAO("Munição", 3),
    AGUA("Água", 2),
    COMIDA("Comida", 1);

    Item(String nome, Integer pontuacao) {
        this.nome = nome;
        this.pontuacao = pontuacao;
    }

    private String nome;
    private Integer pontuacao;

    public Integer getPontuacao() {
        return pontuacao;
    }

}
