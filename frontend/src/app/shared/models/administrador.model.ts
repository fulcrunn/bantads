import { Pessoa } from './pessoa.model';

export class Administrador extends Pessoa {
    constructor(
        cpf: string,
        nome: string,
        email: string,
        telefone: string        
    ) {
        super(cpf, nome, email, telefone); 
    }
}
