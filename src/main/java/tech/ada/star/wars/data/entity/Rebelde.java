package tech.ada.star.wars.data.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Rebelde extends BaseEntity<Long> {

    public Rebelde() {
        super();
    }

    public Rebelde(String nome) {
        this();
        this.nome = nome;
    }

    @NotNull
    @Column(name = "nome", unique = true)
    private String nome;

    @NotNull
    @Column(name = "idade")
    private Integer idade;

    @NotNull
    @Column(name = "genero")
    @Enumerated(EnumType.STRING)
    private Genero genero;

    @OneToOne(cascade = { CascadeType.PERSIST })
    @JoinColumn(name = "id_localizacao")
    private Localizacao localizacao;

    @Column(name = "contador_traidor")
    private Integer contadorTraidor;

    @OneToMany(mappedBy = "rebelde", cascade = { CascadeType.PERSIST })
    private List<Recurso> inventario;

    @PrePersist
    void preInsert() {
        this.contadorTraidor = 0;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public Localizacao getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    public List<Recurso> getInventario() {
        return inventario;
    }

    public void setInventario(List<Recurso> inventario) {
        this.inventario = inventario;
    }

    public Integer getContadorTraidor() {
        return contadorTraidor;
    }

    public void setContadorTraidor(Integer contadorTraidor) {
        this.contadorTraidor = contadorTraidor;
    }
}
