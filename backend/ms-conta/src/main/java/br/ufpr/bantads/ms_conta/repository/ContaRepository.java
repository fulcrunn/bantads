package br.ufpr.bantads.ms_conta.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.ufpr.bantads.ms_conta.DTO.GerentePorContaDTO;
import br.ufpr.bantads.ms_conta.model.Conta;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long>{

    public Optional<Conta> findByNumConta(String numConta);
    public Conta findByIdCliente(Long idCliente);
    public List<Conta> findByIdGerente(Long idGerente);
    //@Query(...): Diz ao Spring para não tentar adivinhar, mas sim usar exatamente esta consulta.
    //SELECT new ... GerentePorContaDTO(c.idGerente, COUNT(c)) ...: Esta é uma sintaxe especial do JPQL. 
    //Significa: "Para cada grupo, crie um new GerentePorContaDTO e passe o idGerente (c.idGerente) e a 
    //contagem (COUNT(c)) para o seu construtor."
    //FROM Conta c GROUP BY c.idGerente: Esta é a lógica SQL/JPQL que agrupa todas as contas pelo idGerente.
    @Query("SELECT new br.ufpr.bantads.ms_conta.DTO.GerentePorContaDTO(c.idGerente, COUNT(c)) " +
           "FROM Conta c " +
           "GROUP BY c.idGerente")
    public List<GerentePorContaDTO> countContasByGerente();   
    
} 