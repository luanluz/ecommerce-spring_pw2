package dev.luanluz.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@DiscriminatorValue("F")
public class PessoaFisica extends Pessoa {
    @Size(min=11, max=11)
    private String cpf;
    @NotBlank
    @Size(min=2, max=50)
    private String nome;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
