import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-alteracao-de-perfil',
  imports: [FormsModule, CommonModule],
  templateUrl: './alteracao-de-perfil.html',
  styleUrl: './alteracao-de-perfil.css'
})
export class AlterarPerfilComponent {

  perfil = {
    nome: '',
    email: '',
    cpf: '000.000.000-00',
    salario: 0,
    endereco: ''
  };

  saldoAtual: number = -300; 
  gerenteNome: string = 'Maria Gerente';

  limite: number = this.perfil.salario / 2;
  mostrouResultado: boolean = false;

  salvar() {
    let novoLimite = this.perfil.salario / 2;
    if (novoLimite < Math.abs(this.saldoAtual)) {
      novoLimite = Math.abs(this.saldoAtual);
    }

    this.limite = novoLimite;
    this.mostrouResultado = true;
  }
}
