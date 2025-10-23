import { Component } from '@angular/core';
import { Gerente } from '../../../shared/models/gerente.model';
import { CommonModule } from '@angular/common';

interface GerenteExtendido {
  gerente: Gerente;
  senha: string;
}

@Component({
  selector: 'app-listagem-gerente',
  imports: [CommonModule],
  templateUrl: './listagem-gerente.html',
  styleUrl: './listagem-gerente.css'
})
export class ListagemGerente {

  gerentes: GerenteExtendido[] = [];

  ngOnInit() {
    this.gerentes = [
      { gerente: new Gerente("111.111.111-11", "Carla Gerente", "carla@email.com", "11999999999"), senha: '123' },
      { gerente: new Gerente("222.222.222-22", "João Gerente", "joao@email.com", "11888888888"), senha: '456' },
      { gerente: new Gerente("333.333.333-33", "Maria Gerente", "maria@email.com", "11777777777"), senha: '789' }
    ];

    this.gerentes.sort((a, b) => a.gerente.nome.localeCompare(b.gerente.nome));
  }
}