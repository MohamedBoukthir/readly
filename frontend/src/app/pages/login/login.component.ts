import { Component } from '@angular/core';
import {LoginRequest} from "../../services/models/login-request";
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/services/authentication.service";
import {TokenService} from "../../services/token/token.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  loginRequest : LoginRequest = {email: '', password: ''};
  errorMessage : Array<String> = [];

  constructor(
    private router : Router,
    private authenticationService : AuthenticationService,
    private tokenService : TokenService
  ) { }

  login() {
    this.errorMessage= [];
    this.authenticationService.login({
      body: this.loginRequest
    }).subscribe({
      next: (res) => {
        this.tokenService.token = res.token as string;
        this.router.navigate(['/']);
      },
      error: (error) => {
        console.log(error);
        if (error.error.validationErrors) {
          this.errorMessage = error.error.validationErrors;
        } else {
          this.errorMessage.push(error.error.error);
      }
    }
    });
  }

}
