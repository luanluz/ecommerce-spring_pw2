package dev.luanluz.model.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Venda implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private LocalDate data;
    @OneToMany(mappedBy = "venda")
    private List<ItemVenda> itensVenda;
    @ManyToOne
    private Pessoa cliente;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Pessoa getCliente() {
        return cliente;
    }

    public void setCliente(Pessoa cliente) {
        this.cliente = cliente;
    }

    public double total() {
        double total = 0;

        for (ItemVenda item : itensVenda)
            total += item.total();

        return total;
    }
}
