export interface ClientePendente {
    id: number;
    nome: string;
    cpf: string;
    salario: number;
    motivoRejeicao?: string;
    status: 'PENDENTE' | 'APROVADO' | 'REJEITADO';
}
