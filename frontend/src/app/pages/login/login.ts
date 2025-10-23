import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { LoginService } from '../../services/login-service';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  credenciaisUsuario = { email: '', senha: 'tads' };
  mensagemErro = "";

  constructor(private loginService: LoginService) { }

  fazerLogin() {
    this.mensagemErro = ""; 
    const sucesso = this.loginService.login(this.credenciaisUsuario);
    if (!sucesso) {
      this.mensagemErro = "E-mail ou senha inválidos.";
    }
  }
}
