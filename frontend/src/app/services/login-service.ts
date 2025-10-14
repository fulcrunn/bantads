import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './auth-service';
import { ClienteService } from './cliente-service';
import { Cliente } from '../shared/models/cliente.model';

const LS_CHAVE = "clientes"
const LS_USER = "user";

// Interface usuario
export interface User {
  email: string;
  senha: string;
  papel: 'cliente' | 'gerente' | 'admin';
  [key: string]: any; // Permite outras propriedades como nome, cpf, etc.
}

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(
    private clienteService: ClienteService,
    private authService: AuthService,
    private router: Router
  ) { }

  /**
   * Tenta autenticar um usuário com as credenciais fornecidas.
   * @param credenciais Objeto com email e senha.
   * @returns Retorna true se o login for bem-sucedido, false caso contrário.
   */
  public login(credenciais: { email: string, senha: string }): boolean {
    // Agora o tipo é Cliente[] e já não dá erro
    const usuarios: Cliente[] = this.clienteService.listarClientesLocalStorage(LS_CHAVE);

    const usuarioEncontrado = usuarios.find(
      user => user.email === credenciais.email
    );

    // O resto da função continua igual e funciona perfeitamente
    if (usuarioEncontrado && credenciais.senha === usuarioEncontrado.senha) {
      this.authService.login(usuarioEncontrado.papel!); // O '!' diz ao TS que temos a certeza que o papel existe
      this.redirecionarPorPapel(usuarioEncontrado.papel!);
      // Salva usuário autenticado no localStorage
      localStorage.setItem(LS_USER, JSON.stringify(usuarioEncontrado));
      return true;
    }
    return false;
  }

  /**
   * Redireciona o usuário para a tela inicial correspondente ao seu papel.
   */
  private redirecionarPorPapel(papel: string): void {
    switch (papel) {
      case 'cliente':
        this.router.navigate(['/cliente/tela-inicial']);
        break;
      case 'gerente':
        this.router.navigate(['/gerente/tela-inicial']);
        break;
      case 'admin':
        this.router.navigate(['/administrador/tela-inicial-adm']);
        break;
      default:
        this.router.navigate(['/login']); // Rota padrão em caso de erro
        break;
    }
  }

  /**
   * Verifica se o localStorage está vazio e, se estiver,
   * cria 3 usuários padrão para fins de teste.
   */
  public inicializarUsuariosParaTeste(): void {
    const usuarios = this.clienteService.listarClientesLocalStorage(LS_CHAVE);
    if (usuarios.length === 0) {
      // Agora criamos instâncias reais de Cliente
      this.clienteService.salvarClienteLocalStorage(
        new Cliente('', 'Ana Cliente', 'cliente@email.com', '', 0, '', '', '', '', '', '', '', 0, 0, 'tads', 'cliente')
      );
      this.clienteService.salvarClienteLocalStorage(
        new Cliente('', 'João Gerente', 'gerente@email.com', '', 0, '', '', '', '', '', '', '', 0, 0, 'tads', 'gerente')
      );
      this.clienteService.salvarClienteLocalStorage(
        new Cliente('', 'Carla Admin', 'admin@email.com', '', 0, '', '', '', '', '', '', '', 0, 0, 'tads', 'admin')
      );
      this.clienteService.salvarClientesTempLocalStorage(
        new Cliente("111111111111", "João Silva", "joao@joao.com", "4198888888", 5000,  "Rua UM", "81050230", "Casa", "20", 'Pilarzinho1', "Curitiba", "Paraná", 5000, 0)
      );
      this.clienteService.salvarClientesTempLocalStorage(
        new Cliente("66666666666", "Pedro Silva", "pedro@pedro.com", "119855556558", 1500, "Rua Dois", "2222222222", "Casa", "20", 'Pilarzinho2', "Campinas", "São Paulo", 1500, 0)
      );
      this.clienteService.salvarClientesTempLocalStorage(
        new Cliente("77777777777", "Tadeu Silva", "tadeu@tadeu.com", "1112345678", 2500, "Rua Três", "33333333333", "Sobrado 1", "203", 'Pilarzinho3', "Campinas", "São Paulo", 2500, 0)
      );
    }
  }
}
