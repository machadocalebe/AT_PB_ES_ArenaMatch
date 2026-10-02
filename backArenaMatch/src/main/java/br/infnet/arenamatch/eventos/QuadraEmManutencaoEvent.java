package br.infnet.arenamatch.eventos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuadraEmManutencaoEvent {
    private Long quadraId;
    private String nomeQuadra;
}
