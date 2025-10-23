import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ModalRejeitarClienteComponent } from '../modal-rejeitarcliente/modal-rejeitarcliente';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Cliente } from '../../../shared/models/cliente.model';
import { ClienteService } from '../../../services/cliente-service';
import { GerenteService } from '../../../services/gerente-service';

const LS_CHAVE = "clientes"
const LS_CHAVE_TEMP = "clientesPendentes"

@Component({
  selector: 'app-tela-inicial',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './tela-inicial-gerente.html',
  styleUrl: './tela-inicial-gerente.css'
})

export class TelaInicialGerente {
  clientesPendentes: any[] = [];
  clientesAprovados: Cliente[] = [];
  clientesRecusados: Cliente[] = [];
  mensagem: string = '';

  constructor(private modalService: NgbModal,private gerenteService: GerenteService
  ) {
  }

  ngOnInit() {
  this.carregarPedidos();
  this.clientesAprovados = this.gerenteService.carregarClientesAprovados();
  }

  carregarPedidos() {
    this.clientesPendentes = this.gerenteService.carregarClientesPendentes();
  }

  aprovar(cliente: Cliente) {
  this.gerenteService.aprovar(cliente);
  // atualiza a tela
  this.clientesPendentes = this.clientesPendentes.filter(c => c.cpf !== cliente.cpf);
  this.clientesAprovados = this.gerenteService.carregarClientesAprovados();

  this.mensagem = `Cliente ${cliente.nome} aprovado com sucesso!`;
}

  removerClienteLocalStorage(cliente: Cliente) {
    this.gerenteService.removerClienteLocalStorage(cliente);
  }

  abrirModalRecusar(cliente: Cliente) {
    const modalRef = this.modalService.open(ModalRejeitarClienteComponent);
    modalRef.componentInstance.cliente = cliente;

    modalRef.result.then(
      () => {
        this.clientesPendentes = this.clientesPendentes.filter(c => c.cpf !== cliente.cpf);
        this.removerClienteLocalStorage(cliente);
        this.mensagem = `Cliente ${cliente.nome} recusado com sucesso!`;
        console.log(`Cliente recusado: ${JSON.stringify(cliente)}`);
        this.clientesRecusados.push(cliente);
      },
      () => {
        this.mensagem = "Ação cancelada.";
      }
    );
  }
}