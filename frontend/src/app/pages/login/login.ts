import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth-service';


@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css'
})


export class Login {
  credenciaisUsuario = { login: '', senha: '' };
  mensagemErro = "";
 

  constructor(private authService: AuthService, private router: Router) { }

  fazerLogin() {
    this.mensagemErro = ""; 
    const sucesso = this.authService.login(this.credenciaisUsuario.login, this.credenciaisUsuario.senha).subscribe({
      next: (response) => {              
              this.redirecionarPorTipo(response.tipo); // Redirect to a protected route
            },
      error: (error) => {
              // Handle login error
              console.error('Login failed:', error);
              this.mensagemErro = "Login ou senha inválidos.";
            },
            complete: () => {
              // Optional: Actions to take when the observable completes
              console.log('Login attempt completed.');
            }
      });
  } // end fazerLogin

  redirecionarPorTipo(tipo: string): void {
      switch (tipo) {
        
        case 'USUARIO': 
          this.router.navigate(['/cliente/tela-inicial']);
          break;
        case 'GERENTE':
          this.router.navigate(['/gerente/tela-inicial']);
          break;
        case 'ADMINISTRADOR': 
          this.router.navigate(['/administrador/tela-inicial-adm']);
          break;
        default:
          this.router.navigate(['/login']); 
          break;
    }
  }
}
