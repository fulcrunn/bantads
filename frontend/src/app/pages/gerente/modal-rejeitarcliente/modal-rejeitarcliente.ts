import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Cliente } from '../../../shared/models/cliente.model';


@Component({
  selector: 'app-modal-rejeitar-cliente',
  imports: [CommonModule, FormsModule],
  templateUrl: './modal-rejeitarcliente.html',
  styleUrl: './modal-rejeitarcliente.css'
})
export class ModalRejeitarClienteComponent {
  @Input() cliente!: Cliente;

  motivoRejeicao: string = '';

  constructor(
    public activeModal: NgbActiveModal,
    private router: Router
  ) {}

  private atualizarCliente() {
    const clientes = JSON.parse(localStorage.getItem('clientes') || '[]') as Cliente[];

    const index = clientes.findIndex(c => c.cpf === this.cliente.cpf);
    if (index !== -1) {
      clientes[index] = this.cliente;
      localStorage.setItem('clientes', JSON.stringify(clientes));
    }
  }

  confirmarRejeicao(): void {
    if (!this.motivoRejeicao.trim()) {
      alert('Por favor, informe o motivo da rejeição.');
      return;
    }

    const resultado = {
      cliente: this.cliente,
      motivo: this.motivoRejeicao
    };

    this.activeModal.close(resultado);

    // this.cliente['status'] = 'Rejeitado';
    // this.cliente['motivoRecusa'] = this.motivoRejeicao;
    // this.cliente['dataRejeicao'] = new Date().toISOString();

    // this.atualizarCliente();

    // // Aqui futuramente você implementa envio real de e-mail usando backend
    // alert(`Cliente rejeitado. Email enviado para ${this.cliente.email}.`);

    // this.activeModal.close();
    // this.router.navigate(['/tela-inicial-gerente']);
  }

  fechar(): void {
    this.activeModal.dismiss('cancel');
  }
}
