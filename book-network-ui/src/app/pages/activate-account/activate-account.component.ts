import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/services';
import { CommonModule } from '@angular/common';
import { CodeInputModule } from "angular-code-input";

@Component({
  selector: 'app-activate-account',
  imports: [CommonModule, CodeInputModule],
  templateUrl: './activate-account.component.html',
  styleUrl: './activate-account.component.scss'
})
export class ActivateAccountComponent {

  message: string = '';
  isOkay: boolean = true;
  submitted: boolean = false;

  constructor(
    private router: Router,
    private authService: AuthenticationService
  ) { }

  onCodeCompleted(token: string) {
    this.confirmAccount(token);
  }
  confirmAccount(token: string) {
    this.authService.confirm({ token }).subscribe(
      {
        next: (): void => {
          this.message = 'Your account has been successfully activated.\nNo you can login';
          this.submitted=true;
          this.isOkay = true;
        },
        error: () => {
          this.message = 'Token is invalid or expired';
          this.submitted = true;
          this.isOkay = false;
        }
      }
    )
  }

  redirectToLogin(): void {
    this.router.navigate(['/login']);
  }
}
