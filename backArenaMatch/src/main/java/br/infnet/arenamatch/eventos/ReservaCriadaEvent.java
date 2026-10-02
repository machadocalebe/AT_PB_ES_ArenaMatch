package br.infnet.arenamatch.eventos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaCriadaEvent {
    private Long reservaId;
    private String nomeLocatario;
    private Long quadraId;
    private LocalDateTime dataHoraInicio;
}
