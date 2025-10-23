import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Cliente } from '../../../shared/models/cliente.model';
import { ClienteService } from '../../../services/cliente-service';

@Component({
  selector: 'app-saque',
  imports: [CommonModule, FormsModule],
  templateUrl: './saque.html',
  styleUrl: './saque.css'
})
export class Saque {  

  cliente: Cliente | undefined
  valorSaque: number = 0;
  saldoAtual: number = 0;  
  limite: number = 0;    
  mensagem: string = '';
  sucesso: boolean = false;

  constructor(private clienteService: ClienteService) {
    this.cliente = JSON.parse(localStorage.getItem('user') || '{}');
    this.saldoAtual = this.clienteService.getSaldo(this.cliente!);
    this.limite = this.cliente ? this.cliente.limite : 0;
    
  }

  sacar(valorSaque: number) {
    const saldoDisponivel = this.saldoAtual + this.limite;

    if (valorSaque <= 0) {
      this.mensagem = "Digite um valor válido para saque.";
      this.sucesso = false;
      return;
    }

    if (valorSaque > saldoDisponivel) {
      this.mensagem = "Saldo insuficiente!";
      this.sucesso = false;
      return;
    }

    if (this.cliente && this.clienteService.sacar(this.cliente, valorSaque)) {
      this.saldoAtual = this.clienteService.getSaldo(this.cliente);
      this.mensagem = `Saque de R$${valorSaque} realizado com sucesso.`;
      this.sucesso = true;
    } else {
      this.mensagem = "Erro ao processar o saque.";
      this.sucesso = false;
    }
  }
}
