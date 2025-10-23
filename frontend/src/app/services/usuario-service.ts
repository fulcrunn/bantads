import { Injectable } from '@angular/core';
import { Cliente } from '../shared/models/cliente.model';

const LS_USER = "user";

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  
  constructor() { }
  // Retorna o usuário logado ou null se não houver
  getUsuarioLogado(): Cliente | null {
    const userStr = localStorage.getItem(LS_USER);
    return userStr ? JSON.parse(userStr) as Cliente : null;
  }
  // Cria o localStorage do usuário
  setUsuarioLogado(usuario: Cliente): void {
    localStorage.setItem(LS_USER, JSON.stringify(usuario));
  }
  // Remove o usuário do localStorage (logout)
  limparUsuario(): void {
    localStorage.removeItem(LS_USER);
  }

  // Verifica se há um usuário logado
  isLogado(): boolean {
    return !!localStorage.getItem(LS_USER);
  }

    
}