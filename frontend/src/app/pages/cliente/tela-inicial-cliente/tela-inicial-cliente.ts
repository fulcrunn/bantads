import { Component, OnInit } from '@angular/core';
import { TelaInicialService } from '../../../services/tela-inicial-service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Cliente } from '../../../shared/models/cliente.model';
import { UsuarioService } from '../../../services/usuario-service';
import { ClienteService, ContaMinDTO } from '../../../services/cliente-service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-tela-inicial',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './tela-inicial-cliente.html',
  styleUrl: './tela-inicial-cliente.css'
})
export class TelaInicialCliente implements OnInit {
  
  usuario: Cliente | undefined;
  saldoConta: number = 0;       
  limiteDisponivel: number = 0;
  valorOperacao: number = 0;
  acaoSelecionada: string | null = null;
  mensagem: string = '';

  private idClienteLogado: string | null = null;

  get saldoTotalDisponivel(): number {
    return this.saldoConta + this.limiteDisponivel;
  }

  constructor(private usuarioService: UsuarioService, private clienteService: ClienteService) {
    const currentUserStr = localStorage.getItem('currentUser');
    if (currentUserStr) {
      const currentUser = JSON.parse(currentUserStr);
      this.idClienteLogado = currentUser.id; 
    }
  }

  ngOnInit(): void {
    const user = this.usuarioService.getUsuarioLogado();
    this.usuario = this.clienteService.getClienteByCpf(user?.cpf || '');
    this.carregarSaldoELimite();    
  }

  carregarSaldoELimite(): void {
    if (!this.idClienteLogado) {
        this.mensagem = 'Erro: ID do cliente não encontrado. Faça login novamente.';
        return;
    }
    // Chamada HTTP para buscar saldo e limite do ms-conta
    this.clienteService.getSaldoLimiteByClienteId(this.idClienteLogado).subscribe({
        next: (contaData: ContaMinDTO) => {
            this.saldoConta = contaData.saldo;
            this.limiteDisponivel = contaData.limite;
            this.mensagem = ''; // Limpa a mensagem de erro
        },
        error: (error) => {
            console.error('Erro ao buscar saldo e limite:', error);
            // Se o erro for 404 (Not Found), pode significar que a conta ainda não foi criada/ativada
            if (error.status === 404) {
                this.mensagem = 'Conta não encontrada ou ainda não está ativa. Aguarde a aprovação.';
            } else {
                this.mensagem = 'Erro de comunicação ao carregar os dados da conta.';
            }
            this.saldoConta = 0;
            this.limiteDisponivel = 0;
        }
    });
  }

  
  /*depositar(valor: number) {
    if (valor > 0) {
      this.clienteService.depositar(this.usuario!, valor);
      this.saldo = this.clienteService.getSaldo(this.usuario!);
      this.mensagem = `Depósito de R$${valor} realizado com sucesso.`;
    }
  }

  sacar(valor: number) {
    if (valor > 0 && valor <= this.saldo) {
      const sucesso = this.clienteService.sacar(this.usuario!, valor);
      if (sucesso) {
        this.saldo = this.clienteService.getSaldo(this.usuario!);
        this.mensagem = `Saque de R$${valor} realizado com sucesso.`;
      } else {
        this.mensagem = 'Erro ao processar o saque.';
      }
      
  }
}

  transferir(sacadorCpf: string, valor: number) {
    if (valor > 0 && valor <= this.saldo) {
      const sucesso = this.clienteService.transferir(sacadorCpf,  valor);
      if (sucesso) {
        this.saldo = this.clienteService.getSaldo(this.usuario!);
        this.mensagem = `Transferência de R$${valor} realizada com sucesso.`;
      } else {
        this.mensagem = 'Erro ao processar a transferência.';
      }
    } else {
      this.mensagem = 'Saldo insuficiente para a transferência.';
    }
  }*/

  consultarExtrato() {
    // Implementar lógica de consulta de extrato
    this.mensagem = 'Funcionalidade de extrato não implementada.';
  }
}
