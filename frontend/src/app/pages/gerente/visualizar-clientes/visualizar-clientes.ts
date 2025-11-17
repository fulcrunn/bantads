import { Component } from '@angular/core';
import { Cliente } from '../../../shared/models/cliente.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NgxMaskPipe } from 'ngx-mask';

@Component({
  selector: 'app-visualizar-clientes',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, NgxMaskPipe],
  templateUrl: './visualizar-clientes.html',
  styleUrl: './visualizar-clientes.css'
})
export class VisualizarClientes {
  pesquisa: string = '';
  clientes: Cliente[] = [];

  constructor() {
    this.carregarClientes();
  }

  carregarClientes() {
    this.clientes = JSON.parse(localStorage.getItem('clientes') || '[]') as Cliente[];
  }

  get clientesFiltrados(): Cliente[] {
    return this.clientes
      .filter(c =>
        c.nome.toLowerCase().includes(this.pesquisa.toLowerCase()) ||
        c.cpf.includes(this.pesquisa)
      )
      .sort((a, b) => a.nome.localeCompare(b.nome));
  }

}