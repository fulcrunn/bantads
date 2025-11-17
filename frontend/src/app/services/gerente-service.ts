import { Injectable } from '@angular/core';
import { Cliente } from '../shared/models/cliente.model';
import { ClienteService } from './cliente-service';
import { HttpClient } from '@angular/common/http';
import { ClientePendente } from '../shared/models/cliente-pendente';
import { Observable } from 'rxjs';

const LS_CHAVE = "clientes"
const LS_CHAVE_TEMP = "clientesPendentes"

@Injectable({
  providedIn: 'root'
})
export class GerenteService {
  private readonly API_URL = 'http://localhost:3000/gerentes'; // URL base
  clientesPendentes: any[] = [];
  clientesAprovados: Cliente[] = [];
  clientesRecusados: Cliente[] = [];
  mensagem: string = '';
  
  private readonly API_URL_PENDENTES = 'http://localhost:3000/gerentes/clientes-pendentes';
  constructor(private http: HttpClient) { }
  
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

  listarClientesPendentes(): Observable<ClientePendente[]>{
    return this.http.get<ClientePendente[]>(`${this.API_URL}/clientes-pendentes`);
  }

  rejeitarCliente(cliente: ClientePendente, motivo: String): Observable<any> {
    return this.http.patch(`${this.API_URL}/clientes/${cliente.id}/rejeitar`,{"motivo": motivo});
  }

  aprovarCliente(cliente: ClientePendente): Observable<any>{ 
    console.log("O id do cliente e: " + cliente.id);   
    return this.http.patch(`${this.API_URL}/clientes/${cliente.id}/aprovar`, cliente);
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
  

  console.log(`Cliente aprovado: ${JSON.stringify(cliente)}`);
} // end aprovar()     
      
    carregarClientesAprovados(): Cliente[] {
      return JSON.parse(localStorage.getItem(LS_CHAVE) || '[]') as Cliente[];
    }
      
}
