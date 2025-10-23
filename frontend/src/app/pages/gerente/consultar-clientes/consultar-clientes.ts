import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ClienteConta } from '../../../shared/models/clienteconta';

@Component({
  selector: 'app-consultar-clientes',
  imports: [CommonModule, FormsModule],
  templateUrl: './consultar-clientes.html',
  styleUrl: './consultar-clientes.css'
})
export class ConsultarClientes {

filtro: string = "";
  clientes: ClienteConta[] = [];
  clientesFiltrados: ClienteConta[] = [];

  constructor() {}

  ngOnInit() {
    this.clientes = [
      new ClienteConta(
        "123.456.789-00", "Ana Souza", "ana@email.com", "11999999999",
        2500, "Rua A", "01001-000", "Ap 101", "123",
        "Bairro1", "São Paulo", "SP", 2000, 1250
      ),
      new ClienteConta(
        "987.654.321-00", "Bruno Lima", "bruno@email.com", "11888888888",
        1800, "Rua B", "02002-000", "Casa", "456",
        "Bairro2", "São Paulo", "SP", -300, 0 
      )
    ];

    // Inicia a lista filtrada
    this.filtrarClientes();
  }

  filtrarClientes() {
    const termo = this.filtro.toLowerCase();

    this.clientesFiltrados = this.clientes
      .filter(c =>
        c.nome.toLowerCase().includes(termo) ||
        c.cpf.toLowerCase().includes(termo)
      )
      .sort((a, b) => a.nome.localeCompare(b.nome));
  }
}