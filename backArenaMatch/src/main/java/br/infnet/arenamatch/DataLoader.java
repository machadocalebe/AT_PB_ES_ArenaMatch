package br.infnet.arenamatch;

import br.infnet.arenamatch.quadras.Quadra;
import br.infnet.arenamatch.quadras.QuadraRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(QuadraRepository repository) {
        return args -> {
            // Agora usamos o nosso construtor personalizado!
            repository.save(new Quadra("Quadra 1 - Society Ouro", 150.0));
            repository.save(new Quadra("Quadra 2 - Beach Tennis", 100.0));
            repository.save(new Quadra("Quadra 3 - Futsal Prata", 120.0));

            System.out.println(" Quadras iniciais carregadas no banco H2!");
        };
    }
}