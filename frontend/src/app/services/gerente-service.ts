import { Injectable } from '@angular/core';
import { Cliente } from '../shared/models/cliente.model';
import { ClienteService } from './cliente-service';

const LS_CHAVE = "clientes"
const LS_CHAVE_TEMP = "clientesPendentes"

@Injectable({
  providedIn: 'root'
})
export class GerenteService {
  clientesPendentes: any[] = [];
  clientesAprovados: Cliente[] = [];
  clientesRecusados: Cliente[] = [];
  mensagem: string = '';

  constructor() {
      }

  calcularLimite(salario: number): number {
  let limite = 0.0;
  console.log(salario + " - " + typeof salario);
  if(salario >= 2000){
    limite = salario/2;
    console.log(`Limite calculado: ${limite} para salário de ${salario}`);
    return limite;
  }else {
    console.log(`Limite recusado calculado: 0.0 para salário de ${salario}`);
    return 0.0;
  }
}
  carregarClientesPendentes(): any{
    const listaClientePendenteAux = JSON.parse(localStorage.getItem(LS_CHAVE_TEMP) || '[]');
    for(let cliente of listaClientePendenteAux){
      cliente.limite = this.calcularLimite(cliente.salario);
      this.clientesPendentes.push(cliente);
    }
    console.log(`Carregar Pedidos = ${JSON.stringify(this.clientesPendentes)}`);
    return this.clientesPendentes;
  }
  
  aprovar(cliente: Cliente): void {
  cliente.status = 'APROVADO';
  cliente.senha = 'tads';

  // remove da lista clientesPendentes
  this.clientesPendentes = this.clientesPendentes.filter(c => c.cpf !== cliente.cpf);
  // salva nos clientesAprovados (LS_CHAVE)
  const clientesAprovados = JSON.parse(localStorage.getItem(LS_CHAVE) || '[]') as Cliente[];
  clientesAprovados.push(cliente);
  localStorage.setItem(LS_CHAVE, JSON.stringify(clientesAprovados));
  // remove do localStorage
  this.removerClienteLocalStorage(cliente);

  console.log(`Cliente aprovado: ${JSON.stringify(cliente)}`);
}  
    
    removerClienteLocalStorage(cliente: Cliente) {
      const clientes = JSON.parse(localStorage.getItem(LS_CHAVE_TEMP) || '[]') as Cliente[];
      const clientesAtualizados = clientes.filter(c => c.cpf !== cliente.cpf);
      localStorage.setItem(LS_CHAVE_TEMP, JSON.stringify(clientesAtualizados));
    }
      
    carregarClientesAprovados(): Cliente[] {
      return JSON.parse(localStorage.getItem(LS_CHAVE) || '[]') as Cliente[];
    }

    carregarClientesRecusados(): Cliente[] {
      return JSON.parse(localStorage.getItem('clientesRecusados') || '[]') as Cliente[];
    }

    atualizarClientesPendentes(clientes: Cliente[]): void {
      localStorage.setItem(LS_CHAVE_TEMP, JSON.stringify(clientes));
      this.clientesPendentes = clientes;
    }
}
