import { Pessoa } from './pessoa.model';

export class Cliente extends Pessoa {

    public status?: 'PENDENTE' | 'APROVADO' | 'REJEITADO';
    public motivoRecusa?: string;
    public dataRejeicao?: string;
    public papel?: 'cliente' | 'gerente' | 'admin';
    public senha?: string;
       
    constructor(
        cpf: string,
        nome: string,
        email: string,
        telefone: string,
        public salario: number,
        public endereco: string,
        public cep: string,
        public complemento: string,
        public numero: string,
        public bairro: string,
        public cidade: string,
        public uf: string,
        public saldo: number,
        public limite: number,
        senha?: string,
        papel?: 'cliente' | 'gerente' | 'admin'
        
    ) {
        super(cpf, nome, email, telefone); 
        this.saldo = saldo ?? 0;
        this.limite = limite ?? 0;
        this.senha = senha;
        this.papel = papel ?? 'cliente'; 
        
    }
}
