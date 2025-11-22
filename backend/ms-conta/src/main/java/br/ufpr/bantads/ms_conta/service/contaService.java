package br.ufpr.bantads.ms_conta.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import br.ufpr.bantads.ms_conta.DTO.ComandoCriarContaDTO;
import br.ufpr.bantads.ms_conta.DTO.GerentePorContaDTO;
import br.ufpr.bantads.ms_conta.model.Conta;
import br.ufpr.bantads.ms_conta.model.Conta.StatusConta;
import br.ufpr.bantads.ms_conta.repository.ContaRepository;

@Service
public class contaService {

    private final ContaRepository contaRepository;

    public contaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }
    
    private String gerarConta(){
        String numConta = "";
        for (int i = 0; i < 4; i++) {
            int digito = (int) (Math.random() * 10);
            numConta += digito;
        }
        return numConta;
    }

    public String gerarNumConta() {
        String numConta = gerarConta();
        while(contaRepository.findByNumConta(numConta).isPresent()){
            numConta = gerarConta();
        }
        return numConta;        
    }

    public BigDecimal calcLimita(BigDecimal salario){
        if(salario.compareTo(new BigDecimal("2000")) >= 0){
            BigDecimal limite = salario.multiply(new BigDecimal("0.5"));
            return limite;
        }else{ return new BigDecimal("0.00");}
       
    }

    public Conta criarConta(Long idCliente, BigDecimal salario, Long idGerente){
        String numConta = gerarNumConta();
        BigDecimal limite = calcLimita(salario);
        BigDecimal saldoInicial = new BigDecimal("0.00");
        Date dataAbertura = new Date();

        // cria a conta
        Conta novaConta = new Conta();
        novaConta.setIdCliente(idCliente);
        novaConta.setIdGerente(idGerente);
        novaConta.setNumConta(numConta);
        novaConta.setLimite(limite);
        novaConta.setSaldo(saldoInicial);
        novaConta.setAbertura(dataAbertura);
        novaConta.setStatus(StatusConta.INATIVA);

        //Salva no banco e retorna o objeto salvo
        return contaRepository.save(novaConta);
    }

    public void ativarConta(Long idCliente){
        Conta conta = contaRepository.findByIdCliente(idCliente);
        if(conta != null){
            conta.setStatus(StatusConta.ATIVA);
            contaRepository.save(conta);
        }
    }

    @RabbitListener(queues = "${rabbitmq.aprovacao.queue.conta}")
    public void onCriarConta(ComandoCriarContaDTO comando) {
        System.out.println("Recebido comando para criar conta para o cliente ID: " + comando.getIdCliente());
        try {            
            criarConta(comando.getIdCliente(), comando.getSalario(), comando.getIdGerente());            
            System.out.println("Conta criada com sucesso para o cliente ID: " + comando.getIdCliente());
            
        } catch (Exception e) {
            System.err.println("Erro ao processar comando de criação de conta para o cliente ID " + comando.getIdCliente() + ": " + e.getMessage());
            
        }
    }

    public List<GerentePorContaDTO> getGerentePorConta() {
        return contaRepository.countContasByGerente();
    }
}
