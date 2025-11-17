import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ClienteService } from '../../../services/cliente-service';
import { Cliente } from '../../../shared/models/cliente.model';

@Component({
  selector: 'app-depositar',
  imports: [FormsModule, CommonModule],
  templateUrl: './depositar.html',
  styleUrl: './depositar.css'
})
export class Depositar {
  clienteLogado: Cliente | undefined;
  valorDeposito: number = 0;
  saldoAtual: number = 0; 
  mensagem: string = '';

  constructor(private clienteService: ClienteService) {
    // Inicializa o saldo atual com o saldo do cliente logado
    this.clienteLogado = JSON.parse(localStorage.getItem('user') || '{}');
    console.log("Cliente logado: ", this.clienteLogado);
    if (this.clienteLogado && this.clienteLogado.cpf) {
      this.saldoAtual = this.clienteService.getSaldo(this.clienteLogado);
      console.log("O saldo atual é: ", this.saldoAtual);
    }
  }

  depositar() {
    if (this.valorDeposito > 0 && this.clienteLogado) {
      this.clienteService.depositar(this.clienteLogado, this.valorDeposito);
      this.saldoAtual = this.clienteService.getSaldo(this.clienteLogado);
      this.mensagem = `Depósito de R$${this.valorDeposito} realizado com sucesso.`;
      this.valorDeposito = 0; // Reseta o valor do depósito após a operação
    } else {
      this.mensagem = 'Por favor, insira um valor válido para depósito.';
    }
       }
  
}