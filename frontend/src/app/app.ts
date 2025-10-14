import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Navbar } from './navbar/navbar';
import { AuthService } from './services/auth-service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UsuarioService } from './services/usuario-service';
import { LoginService } from './services/login-service';


@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Navbar, CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected title = 'banco';

  constructor(public authService: AuthService, public router: Router,private loginService: LoginService) {
    this.loginService.inicializarUsuariosParaTeste();
  }  
}
