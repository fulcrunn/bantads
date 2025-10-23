import { Component, OnInit } from '@angular/core';
import { TelaInicialService } from '../../../services/tela-inicial-service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Cliente } from '../../../shared/models/cliente.model';
import { UsuarioService } from '../../../services/usuario-service';
import { ClienteService } from '../../../services/cliente-service';
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
  saldo: number = 0;
  valorOperacao: number = 0;
  acaoSelecionada: string | null = null;
  mensagem: string = '';

  constructor(private usuarioService: UsuarioService, private clienteService: ClienteService) {}

  ngOnInit(): void {
    const user = this.usuarioService.getUsuarioLogado();
    this.usuario = this.clienteService.getClienteByCpf(user?.cpf || '');
    // Se houver um usuário logado, pega o saldo
    if (this.usuario) {
      this.saldo = this.usuario.saldo;
    } else {
      this.mensagem = 'Nenhum usuário autenticado.';
    }
  }

  depositar(valor: number) {
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
  }

  consultarExtrato() {
    // Implementar lógica de consulta de extrato
    this.mensagem = 'Funcionalidade de extrato não implementada.';
  }
}
