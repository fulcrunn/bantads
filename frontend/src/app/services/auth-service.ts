import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  //Guarda uma string que representa o usuario logado ou null
  private userRole: string | null = null;

  constructor() { }

  // Getter e Setter para o papel do usuario
  public get role(): string | null {
    if (!this.userRole) {
      this.userRole = localStorage.getItem('userRole');
    }
    return this.userRole; 
  }
  // Metodo para simular o login
  public set role(role: string | null) {
    this.userRole = role;    
  }
  public login(role: string): void {
    this.userRole = role;
    // Usamos setItem quando temos um valor
    localStorage.setItem('userRole', role); 
  }
    // Metodo para fechar o login
  public logout(): void {
    this.userRole = null;
    // limpar o localStorage ao fazer logout
    localStorage.removeItem('userRole');    
  } 
} // end class
