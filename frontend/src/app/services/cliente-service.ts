import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Cliente } from '../shared/models/cliente.model';

const LS_CHAVE = "clientes"
const LS_CHAVE_TEMP = "clientesPendentes"

@Injectable({
  providedIn: 'root'
})
export class ClienteService {
  
  // endereço da api de microserviço
  private readonly API_URL = 'http://localhost:3000/api/clientes';
  cliente: Cliente | null = null;
  constructor(private http: HttpClient) { }

  
  buscaCep(cep: string) {
    //lembrar que uma chamada HTTP não é instantânea, ela retorna um "Observable"
    return this.http.get(`${this.API_URL}/cep/${cep}`);
  }

  listarClientesLocalStorage(CHAVE: string): Cliente[]{
    const clientes = localStorage[CHAVE];
    return clientes ? JSON.parse(clientes) : [];
  }
  //lista temporaria para clientes pendentes de aprovação
  salvarClientesTempLocalStorage(cliente: Cliente) {
    const clientesExistentes = this.listarClientesLocalStorage(LS_CHAVE_TEMP);
    clientesExistentes.push(cliente);
    localStorage[LS_CHAVE_TEMP] = JSON.stringify(clientesExistentes);
  }

  salvarClienteLocalStorage(cliente: Cliente) {
    //log de controle, apagar na versão final
    console.log(cliente)
    //##
    const clientes = this.listarClientesLocalStorage(LS_CHAVE);
    clientes.push(cliente);
    localStorage[LS_CHAVE] = JSON.stringify(clientes);
  }

  autocadastro(cliente: Cliente) {
    return this.http.post<Cliente>(this.API_URL, cliente);
  }
  
  getSaldo(cliente: Cliente): number {
    const clientes = this.listarClientesLocalStorage(LS_CHAVE);
    const clienteEncontrado = clientes.find(c => c.cpf === cliente.cpf);
    return clienteEncontrado ? clienteEncontrado.saldo : 0;
  }

  depositar(cliente: Cliente, valor: number): void {
    const clientes = this.listarClientesLocalStorage(LS_CHAVE);
    const index = clientes.findIndex(c => c.cpf === cliente.cpf);
    if (index !== -1 && valor > 0) {
      clientes[index].saldo += valor;
      localStorage[LS_CHAVE] = JSON.stringify(clientes);
    }
  }

  sacar(cliente: Cliente, valor: number): boolean {
    const clientes = this.listarClientesLocalStorage(LS_CHAVE);
    const index = clientes.findIndex(c => c.cpf === cliente.cpf);
    if (index !== -1 && valor > 0 && clientes[index].saldo >= valor) {
      clientes[index].saldo -= valor;
      localStorage[LS_CHAVE] = JSON.stringify(clientes);
      return true;
    }
    return false;
  }

  getClienteByCpf(cpf: String): Cliente | undefined {
    const clientes = this.listarClientesLocalStorage(LS_CHAVE);
    return clientes.find(c => c.cpf === cpf);
  }

  getClienteByCpfTemp(cpf: String): Cliente | undefined {
    const clientes = this.listarClientesLocalStorage(LS_CHAVE_TEMP);
    return clientes.find(c => c.cpf === cpf);
  }

  atualizarCliente(clienteAtualizado: Cliente): void {
    const clientes = this.listarClientesLocalStorage(LS_CHAVE);
    const index = clientes.findIndex(c => c.cpf === clienteAtualizado.cpf);
    if (index !== -1) {
      clientes[index] = clienteAtualizado;
      localStorage[LS_CHAVE] = JSON.stringify(clientes);
    }
  }

  removerCliente(cpf: String): void {
    let clientes = this.listarClientesLocalStorage(LS_CHAVE);
    clientes = clientes.filter(c => c.cpf !== cpf);
    localStorage[LS_CHAVE] = JSON.stringify(clientes);
  }

  transferir(cpf: string, valor: number) {
    const clientes = this.listarClientesLocalStorage(LS_CHAVE);
    const origemCliente = JSON.parse(localStorage.getItem('user') || '{}');
    const indexOrigem = clientes.findIndex(c => c.cpf === origemCliente.cpf);
    const indexDestino = clientes.findIndex(c => c.cpf === cpf);
    //const destinoCliente = clientes[indexDestino];
    //const clienteAux = clientes.findIndex(c=> origemCliente.cpf !== c.cpf && cpf !== c.cpf);
    if (indexOrigem !== -1 && indexDestino !== -1 && valor > 0 && clientes[indexOrigem].saldo >= valor) {
      clientes[indexOrigem].saldo -= valor;
      clientes[indexDestino].saldo += valor;
      localStorage[LS_CHAVE] = JSON.stringify(clientes);
      return true;
    }
    return false;    
  }

}