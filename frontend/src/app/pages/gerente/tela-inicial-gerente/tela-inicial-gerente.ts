import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ModalRejeitarClienteComponent } from '../modal-rejeitarcliente/modal-rejeitarcliente';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Cliente } from '../../../shared/models/cliente.model';
import { ClienteService } from '../../../services/cliente-service';
import { GerenteService } from '../../../services/gerente-service';
import { ClientePendente } from '../../../shared/models/cliente-pendente';

const LS_CHAVE = "clientes"
const LS_CHAVE_TEMP = "clientesPendentes"

@Component({
  selector: 'app-tela-inicial',
  standalone: true,
  imports: [CommonModule, ModalRejeitarClienteComponent],
  templateUrl: './tela-inicial-gerente.html',
  styleUrl: './tela-inicial-gerente.css'
})

export class TelaInicialGerente {
  clientesPendentes: ClientePendente[] = [];
  clientesAprovados: Cliente[] = [];
  
  mensagem: string = '';

  constructor(private modalService: NgbModal,private gerenteService: GerenteService
  ) {
  }

  ngOnInit() {
  this.listarPendentes();
  console.log(this.clientesPendentes)
  
  }

  listarPendentes() {
  this.gerenteService.listarClientesPendentes()
    .subscribe({
      next: (listaRecebida) => { 
        this.clientesPendentes = listaRecebida;
        console.log('Clientes Pendentes Recebidos:', listaRecebida);
      },
      error: (erro) => {
        console.error('Erro ao buscar clientes pendentes:', erro);
        this.mensagem = 'Erro ao carregar clientes pendentes.';
      }
    });
}  

  aprovar(cliente: ClientePendente) {
  this.gerenteService.aprovarCliente(cliente).subscribe({
  next: (resposta) => {
    console.log('Sucesso:', resposta);
    this.mensagem = `Cliente ${cliente.nome} aprovado com sucesso!`;
    // Atualize a lista de clientes ou mostre mensagem de sucesso aqui
  },
  error: (erro) => {
    console.error('Erro:', erro);
    this.mensagem = `Cliente ${cliente.nome} Falha na aprovação!`;
    // Trate o erro aqui
  }
});
  // atualiza a tela
  this.clientesPendentes = this.clientesPendentes.filter(c => c.cpf !== cliente.cpf);
  this.clientesAprovados = this.gerenteService.carregarClientesAprovados();

  
}
  abrirModalRecusar(cliente: ClientePendente) {
    // usando o padrão Publish/Subscribe com uma Exchange do tipo FanoutExchange
    const modalRef = this.modalService.open(ModalRejeitarClienteComponent);
    modalRef.componentInstance.cliente = cliente;

    modalRef.result.then(
      () => {
        this.clientesPendentes = this.clientesPendentes.filter(c => c.cpf !== cliente.cpf);
        this.mensagem = `Cliente ${cliente.nome} recusado com sucesso!`;
        console.log(`Cliente recusado: ${JSON.stringify(cliente)}`);        
      },
      () => {
        this.mensagem = "Ação cancelada.";
      }
    );
  }
}