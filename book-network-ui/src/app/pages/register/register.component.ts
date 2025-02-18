import { Component } from '@angular/core';
import { RegistrationRequest } from '../../services/models';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AuthenticationService } from '../../services/services';

@Component({
  selector: 'app-register',
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {

  registerRequest: RegistrationRequest = {
    email: '',
    firstname: '',
    lastname: '',
    password: ''
  }

  errorMessages: Array<string> = [];

  constructor(private router: Router, private authService: AuthenticationService) { }

  register(): void {
    this.errorMessages = [];
    this.authService.register({ body: this.registerRequest }).subscribe({
      next: (): void => {
        this.router.navigate(['/activate-account']);
      },
      error: (err) => {
        this.errorMessages = err.error.validationErrors;
      }
    })

  }

  login(): void {
    this.router.navigate(['/login']);
  }

}
