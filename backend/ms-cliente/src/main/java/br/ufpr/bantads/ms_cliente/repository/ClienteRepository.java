package br.ufpr.bantads.ms_cliente.repository;

import br.ufpr.bantads.ms_cliente.model.Cliente;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // métodos em uma interface definem um contrato público
    public Optional<Cliente> findByCpf(String cpf) ;

    public Optional<Cliente> findByEmail(String email);
    
}
