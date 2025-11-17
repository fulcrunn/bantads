export interface ClientePendente {
    id: number;
    idGerente: number;
    nome: string;
    cpf: string;
    email: string;
    salario: number;
    motivoRejeicao?: string;
    status: 'PENDENTE' | 'APROVADO' | 'REJEITADO';
}
