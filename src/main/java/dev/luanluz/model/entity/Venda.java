package dev.luanluz.model.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Scope("session")
@Component
public class Venda implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private LocalDate data;
    @OneToMany(mappedBy = "venda", cascade = CascadeType.PERSIST)
    @Valid
    private List<ItemVenda> itensVenda = new ArrayList<>();
    @ManyToOne
    @Valid
    private Pessoa pessoa;
    @ManyToOne
    @Valid
    private Endereco endereco;

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

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa cliente) {
        this.pessoa = cliente;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public List<ItemVenda> getItensVenda() {
        return itensVenda;
    }

    public void setItensVenda(List<ItemVenda> itensVenda) {
        this.itensVenda = itensVenda;
    }

    public double total() {
        double total = 0;

        for (ItemVenda item : itensVenda)
            total += item.total() * item.getQtd();

        return total;
    }
}
