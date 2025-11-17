import { Component } from '@angular/core';
import { AuthService } from '../services/auth-service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { UsuarioService } from '../services/usuario-service';

@Component({
  selector: 'app-navbar',
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class Navbar {

  constructor(public authService: AuthService, private router: Router, private userService: UsuarioService) { }
  logout() {
    this.userService.limparUsuario();
    this.authService.logout();
    alert('Logout efetuado com sucesso!');
    this.router.navigate(['/login']);
}
}// end class
