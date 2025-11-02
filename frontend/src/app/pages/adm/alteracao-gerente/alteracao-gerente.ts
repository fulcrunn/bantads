import { Component } from '@angular/core';
import { Gerente } from '../../../shared/models/gerente.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';


interface GerenteExtendido {
  gerente: Gerente;
  senha: string;
}

@Component({
  selector: 'app-alteracao-gerente',
  imports: [CommonModule, FormsModule],
  templateUrl: './alteracao-gerente.html',
  styleUrl: './alteracao-gerente.css'
})
export class AlteracaoGerente {

  gerentes: GerenteExtendido[] = [];
  gerenteSelecionado?: GerenteExtendido;

  ngOnInit() {
    this.gerentes = [
      { gerente: new Gerente("111.111.111-11", "Carla Gerente", "carla@email.com", "11999999999"), senha: '123' },
      { gerente: new Gerente("222.222.222-22", "João Gerente", "joao@email.com", "11888888888"), senha: '456' },
      { gerente: new Gerente("333.333.333-33", "Maria Gerente", "maria@email.com", "11777777777"), senha: '789' }
    ];
  }

  selecionarGerente(gerente: GerenteExtendido) {
    // Criar uma cópia para edição
    this.gerenteSelecionado = {
      gerente: new Gerente(
        gerente.gerente.cpf,
        gerente.gerente.nome,
        gerente.gerente.email,
        gerente.gerente.telefone
      ),
      senha: gerente.senha
    };
  }

  salvarAlteracoes() {
    if (!this.gerenteSelecionado) return;

    const index = this.gerentes.findIndex(g => g.gerente.cpf === this.gerenteSelecionado!.gerente.cpf);
    if (index !== -1) {
      this.gerentes[index] = this.gerenteSelecionado;
      alert("Gerente atualizado com sucesso!");
      this.gerenteSelecionado = undefined;
    }
  }
}
