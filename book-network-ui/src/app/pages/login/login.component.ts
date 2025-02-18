import { Component } from '@angular/core';
import { AuthenticationRequest, AuthenticationResponse } from '../../services/models';
import { FormsModule } from "@angular/forms";
import { CommonModule } from '@angular/common';
import { AuthenticationService } from '../../services/services';
import { Router, RouterLink } from '@angular/router';
import { TokenService } from '../../services/token/token.service';

@Component({
  selector: 'app-login',
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})

export class LoginComponent {
  authRequest: AuthenticationRequest = { email: "", password: "" };
  errorMessages: Array<string> = [];

  constructor(
    private router: Router,
    private authService: AuthenticationService,
    private tokenService: TokenService
  ) {

  }

  login(): void {
    this.errorMessages = [];
    this.authService.authenticate({ body: this.authRequest }).subscribe({
      next: (res: AuthenticationResponse): void => {
        this.tokenService.token = res.token as string;
        this.router.navigate(["/books"]);
      },
      error: (err: any): void => {
        if (err.error.validationErrors) {
          this.errorMessages = err.error.validationErrors
        } else {
          this.errorMessages.push(err.error.error)
        }
      }
    })

  }
  register(): void {
    this.router.navigate(["/register"])
  }
}
