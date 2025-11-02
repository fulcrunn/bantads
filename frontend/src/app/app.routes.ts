import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { Autocadastro } from './pages/cliente/autocadastro/autocadastro';
import { TelaInicialCliente } from './pages/cliente/tela-inicial-cliente/tela-inicial-cliente';
import { TelaInicialGerente } from './pages/gerente/tela-inicial-gerente/tela-inicial-gerente';
import { AlterarPerfilComponent } from './pages/cliente/alteracao-de-perfil/alteracao-de-perfil';
import { Depositar } from './pages/cliente/depositar/depositar';
import { Saque } from './pages/cliente/saque/saque';
import { Transferencia } from './pages/cliente/transferencia/transferencia';
import { VisualizarClientes } from './pages/gerente/visualizar-clientes/visualizar-clientes';
import { AprovarCliente } from './pages/gerente/aprovar-cliente/aprovar-cliente';
import { ModalRejeitarClienteComponent } from './pages/gerente/modal-rejeitarcliente/modal-rejeitarcliente';
import { ConsultarClientes } from './pages/gerente/consultar-clientes/consultar-clientes';
import { TelaInicialAdm } from './pages/adm/tela-inicial-adm/tela-inicial-adm';
import { RelatorioCliente } from './pages/adm/relatorio-cliente/relatorio-cliente';
import { InsercaoGerente } from './pages/adm/insercao-gerente/insercao-gerente';
import { RemocaoGerente } from './pages/adm/remocao-gerente/remocao-gerente';
import { ListagemGerente } from './pages/adm/listagem-gerente/listagem-gerente';
import { AlteracaoGerente } from './pages/adm/alteracao-gerente/alteracao-gerente';


export const routes: Routes = [
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: 'login', component: Login },
    { path: 'autocadastro', component: Autocadastro},
    { path: 'cliente/tela-inicial', component: TelaInicialCliente},
    { path: 'alteracao-perfil', component: AlterarPerfilComponent},
    { path: 'deposito', component: Depositar},
    { path: 'saque', component: Saque},
    { path: 'transferencia', component: Transferencia},
    { path: 'gerente/tela-inicial', component: TelaInicialGerente},
    { path: 'gerente/visualizar-clientes', component: VisualizarClientes},
    { path: 'gerente/aprovar-cliente', component: AprovarCliente},
    { path: 'gerente/consultar-cliente', component: ConsultarClientes},
    { path: 'administrador/tela-inicial-adm', component: TelaInicialAdm},
    { path: 'administrador/relatorio-cliente', component: RelatorioCliente},
    { path: 'administrador/insercao-gerente', component: InsercaoGerente},
    { path: 'administrador/remocao-gerente', component: RemocaoGerente},
    { path: 'administrador/listagem-gerente', component: ListagemGerente},
    { path: 'administrador/alterar-gerente', component: AlteracaoGerente}
];

