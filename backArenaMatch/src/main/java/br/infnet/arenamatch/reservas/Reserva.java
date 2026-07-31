package br.infnet.arenamatch.reservas;

import br.infnet.arenamatch.quadras.Quadra;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@Entity
@Audited
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeLocatario;
    private LocalDateTime dataHoraInicio;

    // Relacionamento com o Bounded Context de Quadras
    @ManyToOne
    @JoinColumn(name = "quadra_id")
    private Quadra quadra;
}