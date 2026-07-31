package br.infnet.arenamatch.quadras;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quadra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private Double precoHora;

    private boolean emManutencao = false;

    // Novo construtor limpo e focado no negócio
    public Quadra(String nome, Double precoHora) {
        this.nome = nome;
        this.precoHora = precoHora;
        // O id será preenchido automaticamente pelo banco de dados
        // A manutenção já nasce como 'false' pelo valor padrão do atributo
    }
}