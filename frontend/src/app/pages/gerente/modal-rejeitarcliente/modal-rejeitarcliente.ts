import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { GerenteService } from '../../../services/gerente-service';
import { ClientePendente } from '../../../shared/models/cliente-pendente';


@Component({
  selector: 'app-modal-rejeitar-cliente',
  imports: [CommonModule, FormsModule],
  templateUrl: './modal-rejeitarcliente.html',
  styleUrl: './modal-rejeitarcliente.css'
})
export class ModalRejeitarClienteComponent {
  @Input() cliente!: ClientePendente;

  motivoRejeicao: string = '';

  constructor(
    public activeModal: NgbActiveModal,
    private router: Router,
    private gerenteService: GerenteService
  ) {}

  
  confirmarRejeicao(): void {
    if (!this.motivoRejeicao.trim()) {
      alert('Por favor, informe o motivo da rejeição.');
      return;
    }

    const resultado = {
      cliente: this.cliente,
      motivo: this.motivoRejeicao
    };
    this.gerenteService.rejeitarCliente(this.cliente, this.motivoRejeicao).subscribe({
      next: () => {
        this.activeModal.close(resultado);
      },
      error: (erro) => {
        console.error('Erro ao rejeitar cliente:', erro);
        alert('Ocorreu um erro ao rejeitar o cliente. Por favor, tente novamente.');
      }
    }); 

    
  } //end rejeitar

  fechar(): void {
    this.activeModal.dismiss('cancel');
  }
}
