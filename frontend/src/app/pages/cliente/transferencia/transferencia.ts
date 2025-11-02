import { Component } from '@angular/core';
import { Cliente } from '../../../shared/models/cliente.model';
import { ClienteService } from '../../../services/cliente-service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-transferencia',
  imports: [FormsModule, CommonModule],
  templateUrl: './transferencia.html',
  styleUrl: './transferencia.css'
})
export class Transferencia {
  cpfDestino: string = '';
  valor: number = 0;
  mensagem: string = '';
  sucesso: boolean = false;

  constructor(private clienteService: ClienteService) {}

  realizarTransferencia(cpf: string, valor: number) {
    this.clienteService.transferir(cpf, valor);
    this.sucesso = true;
    this.mensagem = 'Transferência realizada com sucesso!';
    }
}
