package aula0203.atividade.model.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class ItemVenda implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private double qtd;
    @OneToOne
    private Produto produto;
    @ManyToOne
    private Venda venda;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getQtd() {
        return qtd;
    }

    public void setQtd(double qtd) {
        this.qtd = qtd;
    }

    public double total() {
        return qtd * produto.getValor();
    }
}
