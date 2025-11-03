package br.ufpr.bantads.ms_conta.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.ufpr.bantads.ms_conta.model.Conta;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long>{

    public Optional<Conta> findByNumConta(String numConta);
    public Conta findByIdCliente(Long idCliente);
    public List<Conta> findByIdGerente(Long idGerente);    
    
} 