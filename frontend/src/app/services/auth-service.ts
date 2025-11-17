import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginResponse } from '../shared/models/loginResponse.model';
import { Observable, tap } from 'rxjs';

const AUTH_TOKEN_KEY = 'authToken';
const CURRENT_USER_KEY = 'currentUser';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  
  private readonly API_URL = 'http://localhost:3000/auth';
  constructor(private http: HttpClient) { }
  loginResponse!: LoginResponse;
  
  public login(login: string, senha: string): Observable<LoginResponse> {
    const credentials = { login, senha };
    return this.http.post<LoginResponse>(`${this.API_URL}/login`, credentials)
      .pipe( // Usar o pipe para adicionar operadores RxJS
        tap(response => { // tap executa uma ação secundária com a resposta
          // Guardar o token e os dados do utilizador AQUI
          this.loginResponse = response;
          localStorage.setItem(AUTH_TOKEN_KEY, response.access_token);
          const currentUser = {
            id: response.user.id,
            login: response.user.login,
            tipo: response.tipo
          };
          localStorage.setItem(CURRENT_USER_KEY, JSON.stringify(currentUser));
          console.log('Token and user info stored in localStorage by AuthService');
        })
      );
  }

  public logout(): void {
    localStorage.removeItem('authToken');
    localStorage.removeItem('currentUser');
  }
} // end class
