package org.example.consumonutrireceitas.model;

public class Receita {
    private Integer id;
    private Integer usuarioId;
    private String nome;
    private String ingredientes;
    private Integer tempoPreparo;
    private Integer serve;
    private String descricao;

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public Integer getTempoPreparo() {
        return tempoPreparo;
    }

    public void setTempoPreparo(Integer tempoPreparo) {
        this.tempoPreparo = tempoPreparo;
    }

    public Integer getServe() {
        return serve;
    }

    public void setServe(Integer serve) {
        this.serve = serve;
    }

    public String getDescricao() {
        return descricao;
    }


    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usarioId) {
        this.usuarioId = usarioId;
    }
}
