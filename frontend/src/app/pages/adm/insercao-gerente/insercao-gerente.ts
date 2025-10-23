import { Component } from '@angular/core';
import { Gerente } from '../../../shared/models/gerente.model';
import { ClienteConta } from '../../../shared/models/clienteconta';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

interface GerenteExtendido {
  gerente: Gerente;
  senha: string;
  contas: ClienteConta[];
}

@Component({
  selector: 'app-insercao-gerente',
  imports: [CommonModule, FormsModule],
  templateUrl: './insercao-gerente.html',
  styleUrl: './insercao-gerente.css'
})
export class InsercaoGerente {

 novoGerente: { cpf: string; nome: string; email: string; telefone: string; senha: string } = {
    cpf: '',
    nome: '',
    email: '',
    telefone: '',
    senha: ''
  };
  gerentes: GerenteExtendido[] = [];

  ngOnInit() {
    const g1 = new Gerente("111.111.111-11", "Carla Gerente", "carla@email.com", "11999999999");
    const g2 = new Gerente("222.222.222-22", "João Gerente", "joao@email.com", "11888888888");

    this.gerentes = [
      {
        gerente: g1,
        senha: '123',
        contas: [
          new ClienteConta("123.456.789-00", "Ana", "ana@email.com", "11999999999",
            2500, "Rua A", "01001-000", "Ap 101", "123", "Bairro1", "São Paulo", "SP", 2000, 1250),
          new ClienteConta("124.456.789-00", "Bruno", "bruno@email.com", "11888888888",
            1800, "Rua B", "02002-000", "Casa", "456", "Bairro2", "São Paulo", "SP", -500, 0)
        ]
      },
      {
        gerente: g2,
        senha: '456',
        contas: [
          new ClienteConta("125.456.789-00", "Carlos", "carlos@email.com", "11777777777",
            2200, "Rua C", "03003-000", "Ap 201", "789", "Bairro1", "São Paulo", "SP", 1000, 1000)
        ]
      }
    ];
  }

  inserirGerente() {
    const novo = new Gerente(
      this.novoGerente.cpf,
      this.novoGerente.nome,
      this.novoGerente.email,
      this.novoGerente.telefone
    );

    let contaTransferida: ClienteConta | null = null;

    if (this.gerentes.length > 0) {
      let maxContas = Math.max(...this.gerentes.map(g => g.contas.length));
      let candidatos = this.gerentes.filter(g => g.contas.length === maxContas);

      if (candidatos.length > 1) {
        candidatos = candidatos.sort((a, b) => {
          const saldoA = a.contas.filter(c => c.saldo >= 0).reduce((s, c) => s + c.saldo, 0);
          const saldoB = b.contas.filter(c => c.saldo >= 0).reduce((s, c) => s + c.saldo, 0);
          return saldoA - saldoB;
        });
      }

      const escolhido = candidatos[0];
      if (escolhido.contas.length > 1 || this.gerentes.length > 1) {
        contaTransferida = escolhido.contas.pop()!; 
      }
    }

    this.gerentes.push({
      gerente: novo,
      senha: this.novoGerente.senha,
      contas: contaTransferida ? [contaTransferida] : []
    });

    alert(`Gerente ${novo.nome} inserido com sucesso!`);

    this.novoGerente = { cpf: '', nome: '', email: '', telefone: '', senha: '' };
  }
}