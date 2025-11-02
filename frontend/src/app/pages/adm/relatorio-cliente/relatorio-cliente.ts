import { Component } from '@angular/core';
import { ClienteConta } from '../../../shared/models/clienteconta';
import { Gerente } from '../../../shared/models/gerente.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';


interface ClienteRelatorio {
  cliente: ClienteConta;
  gerente: Gerente;
}

@Component({
  selector: 'app-relatorio-cliente',
  imports: [CommonModule, FormsModule],
  templateUrl: './relatorio-cliente.html',
  styleUrl: './relatorio-cliente.css'
})

export class RelatorioCliente {

filtro: string = '';
  clientes: ClienteRelatorio[] = [];
  clientesFiltrados: ClienteRelatorio[] = [];

  ngOnInit() {
    const gerentes: Gerente[] = [
      new Gerente("111.111.111-11", "Carla Gerente", "carla@email.com", "11999999999"),
      new Gerente("222.222.222-22", "João Gerente", "joao@email.com", "11888888888")
    ];
    const clientes: { [cpfGerente: string]: ClienteConta[] } = {
      "111.111.111-11": [
        new ClienteConta("123.456.789-00", "Ana Souza", "ana@email.com", "11999999999",
          2500, "Rua A", "01001-000", "Ap 101", "123", "Bairro1", "São Paulo", "SP", 2000, 1250),
        new ClienteConta("124.456.789-00", "Bruno Lima", "bruno@email.com", "11888888888",
          1800, "Rua B", "02002-000", "Casa", "456", "Bairro2", "São Paulo", "SP", -500, 0,)
      ],
      "222.222.222-22": [
        new ClienteConta("125.456.789-00", "Carlos Silva", "carlos@email.com", "11777777777",
          2200, "Rua C", "03003-000", "Ap 201", "789", "Bairro1", "São Paulo", "SP",1000, 1000,)
      ]
    };

    this.clientes = [];
    Object.keys(clientes).forEach(cpfG => {
      const g = gerentes.find(x => x.cpf === cpfG)!;
      clientes[cpfG].forEach(c => {
        this.clientes.push({ cliente: c, gerente: g });
      });
    });

    this.filtrarClientes();
  }

  filtrarClientes() {
    const termo = this.filtro.toLowerCase();
    this.clientesFiltrados = this.clientes
      .filter(c =>
        c.cliente.nome.toLowerCase().includes(termo) ||
        c.cliente.cpf.toLowerCase().includes(termo)
      )
      .sort((a, b) => a.cliente.nome.localeCompare(b.cliente.nome));
  }
}