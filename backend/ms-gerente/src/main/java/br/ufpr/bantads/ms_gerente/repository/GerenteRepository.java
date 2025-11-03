package br.ufpr.bantads.ms_gerente.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.ufpr.bantads.ms_gerente.model.Gerente;
import java.util.Optional;

@Repository
public interface GerenteRepository extends JpaRepository<Gerente, Long> {

    public Optional<Gerente> findByCpf(String cpf);
    public Optional<Gerente> findByEmail(String email);    
    public boolean existsByCpf(String cpf);
    public boolean existsByEmail(String email);

    
} 