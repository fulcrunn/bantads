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
  selector: 'app-remocao-gerente',
  imports: [CommonModule, FormsModule],
  templateUrl: './remocao-gerente.html',
  styleUrl: './remocao-gerente.css'
})
export class RemocaoGerente {

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
            2500, "Rua A", "01001-000", "Ap 101", "123", "Bairro1", "São Paulo", "SP", 2000, 1250, ),
          new ClienteConta("124.456.789-00", "Bruno", "bruno@email.com", "11888888888",
            1800, "Rua B", "02002-000", "Casa", "456", "Bairro2", "São Paulo", "SP", -500, 0,)
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

  removerGerente(cpf: string) {
    if (this.gerentes.length <= 1) {
      alert("Não é possível remover o último gerente do banco!");
      return;
    }

    const index = this.gerentes.findIndex(g => g.gerente.cpf === cpf);
    if (index === -1) return;

    const gerenteRemovido = this.gerentes[index];

    let minContas = Math.min(...this.gerentes.filter((_, i) => i !== index).map(g => g.contas.length));
    let destinatario = this.gerentes.filter((g, i) => i !== index && g.contas.length === minContas)[0];

    gerenteRemovido.contas.forEach(c => destinatario.contas.push(c));

    this.gerentes.splice(index, 1);

    alert(`Gerente ${gerenteRemovido.gerente.nome} removido com sucesso! Contas transferidas para ${destinatario.gerente.nome}.`);
  }
}