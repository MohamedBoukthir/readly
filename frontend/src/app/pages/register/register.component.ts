import {Component} from '@angular/core';
import {RegisterRequest} from "../../services/models/register-request";
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/services/authentication.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  registerRequest: RegisterRequest = {email: '', firstname: '', lastname: '', password: ''};
  errorMessage: Array<string> = [];

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService
  ) {
  }

  register() {
    this.errorMessage = [];
    this.authenticationService.register({
      body: this.registerRequest
    }).subscribe({
      next: () => {
        this.router.navigate(['/activate-account']);
      },
      error: (error) => {
        this.errorMessage = error.error.validationErrors;
      }
    })
  }
}
