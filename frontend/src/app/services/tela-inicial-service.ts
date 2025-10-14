import { Injectable } from '@angular/core';

const LS_SALDO = 'saldoCliente';
const LS_EXTRATO = 'extratoCliente';

@Injectable({
  providedIn: 'root'
})
export class TelaInicialService {

  constructor() {
    // inicializa saldo caso não exista
    if (!localStorage.getItem(LS_SALDO)) {
      localStorage.setItem(LS_SALDO, '0');
    }
    if (!localStorage.getItem(LS_EXTRATO)) {
      localStorage.setItem(LS_EXTRATO, JSON.stringify([]));
    }
  }

  obterSaldo(): number {
    return parseFloat(localStorage.getItem(LS_SALDO) || '0');
  }

  atualizarSaldo(novoSaldo: number): void {
    localStorage.setItem(LS_SALDO, novoSaldo.toString());
  }

  registrarOperacao(tipo: string, valor: number): void {
    const extrato = JSON.parse(localStorage.getItem(LS_EXTRATO) || '[]');
    const registro = {
      tipo,
      valor,
      data: new Date().toLocaleString()
    };
    extrato.push(registro);
    localStorage.setItem(LS_EXTRATO, JSON.stringify(extrato));
  }

  depositar(valor: number): void {
    let saldo = this.obterSaldo();
    saldo += valor;
    this.atualizarSaldo(saldo);
    this.registrarOperacao('Depósito', valor);
  }

  sacar(valor: number): boolean {
    let saldo = this.obterSaldo();
    if (saldo >= valor) {
      saldo -= valor;
      this.atualizarSaldo(saldo);
      this.registrarOperacao('Saque', -valor);
      return true;
    }
    return false;
  }

  transferir(valor: number): boolean {
    let saldo = this.obterSaldo();
    if (saldo >= valor) {
      saldo -= valor;
      this.atualizarSaldo(saldo);
      this.registrarOperacao('Transferência', -valor);
      return true;
    }
    return false;
  }

  obterExtrato(): any[] {
    return JSON.parse(localStorage.getItem(LS_EXTRATO) || '[]');
  }
}
