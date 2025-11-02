import { Cliente } from './cliente.model';

export class ClienteConta extends Cliente {
  constructor(
    cpf: string,
    nome: string,
    email: string,
    telefone: string,
    salario: number,
    endereco: string,
    cep: string,
    complemento: string,
    numero: string,
    bairro:string,
    cidade: string,
    uf: string,   // mudei para `uf` para não conflitar com `estado` do cliente
    saldo?: number, //Tá lá no cliente.modal
    limite?: number, //Tá lá no cliente.modal
  ) {
    super(
      cpf,
      nome,
      email,
      telefone,
      salario,
      endereco,
      cep,
      complemento,
      numero,
      bairro,
      cidade,
      uf,
      saldo ?? 0,
      limite ?? 0
    );
  }
}
