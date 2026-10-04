package br.infnet.arenamatch.avaliacoes;

import jakarta.persistence.*;

@Entity
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quadraId;
    private String autor;
    private Integer nota;
    private String comentario;

    public Avaliacao() {}

    public Avaliacao(Long quadraId, String autor, Integer nota, String comentario) {
        this.quadraId = quadraId;
        this.autor = autor;
        this.nota = nota;
        this.comentario = comentario;
    }

    public Long getId() { return id; }
    public Long getQuadraId() { return quadraId; }
    public String getAutor() { return autor; }
    public Integer getNota() { return nota; }
    public String getComentario() { return comentario; }
}
