import { Component } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { Gerente } from '../../../shared/models/gerente.model';
import { ClienteConta } from '../../../shared/models/clienteconta';

interface GerenteDashboard {
  gerente: Gerente;
  qtdClientes: number;
  saldoPositivo: number;
  saldoNegativo: number;
  clientes: ClienteConta[];
}

@Component({
  selector: 'app-tela-inicial-adm',
  standalone: true,
  imports: [CommonModule, CurrencyPipe],
  templateUrl: './tela-inicial-adm.html',
  styleUrls: ['./tela-inicial-adm.css']
})
export class TelaInicialAdm {

  gerentesDashboard: GerenteDashboard[] = [];

  ngOnInit() {
    const gerentes: Gerente[] = [
      new Gerente("111.111.111-11", "Carla Gerente", "carla@email.com", "11999999999"),
      new Gerente("222.222.222-22", "João Gerente", "joao@email.com", "11888888888"),
      new Gerente("333.333.333-33", "Maria Gerente", "maria@email.com", "11777777777")
    ];

    const clientes: { [cpfGerente: string]: ClienteConta[] } = {
      "111.111.111-11": [
        new ClienteConta("123", "Ana", "ana@email.com", "11999999999", 2500, "Rua A", "01001-000", "Ap 101", "123", "Bairro1", "São Paulo", "SP", 2000, 1250),
        new ClienteConta("124", "Bruno", "bruno@email.com", "11888888888", 1800, "Rua B", "02002-000", "Casa", "456", "Bairro2", "São Paulo", "SP", -500, 0)
      ],
      "222.222.222-22": [
        new ClienteConta("125", "Carlos", "carlos@email.com", "11777777777", 2000, "Rua C", "03003-000", "Ap 201", "789", "Bairro3", "São Paulo", "SP", 1000, 1000)
      ],
      "333.333.333-33": [
        new ClienteConta("126", "Daniela", "daniela@email.com", "11666666666", 2200, "Rua D", "04004-000", "Ap 301", "321", "Bairro4", "São Paulo", "SP", -200, 0)
      ]
    };

    // Monta o dashboard
    this.gerentesDashboard = gerentes.map(g => {
      const clientesGerente = clientes[g.cpf] || [];
      const saldoPositivo = clientesGerente.reduce((acc, c) => acc + (c.saldo > 0 ? c.saldo : 0), 0);
      const saldoNegativo = clientesGerente.reduce((acc, c) => acc + (c.saldo < 0 ? c.saldo : 0), 0);
      return {
        gerente: g,
        qtdClientes: clientesGerente.length,
        saldoPositivo,
        saldoNegativo,
        clientes: clientesGerente
      };
    });

    // Ordena pelos maiores saldos positivos
    this.gerentesDashboard.sort((a, b) => b.saldoPositivo - a.saldoPositivo);
  }
}
