import {Component, OnInit} from '@angular/core';
// import {LoginRequest} from "../../services/models/login-request";
// import {Router} from "@angular/router";
// import {AuthenticationService} from "../../services/services/authentication.service";
// import {TokenService} from "../../services/token/token.service";
import {KeycloakService} from "../../services/keycloak/keycloak.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {

  // loginRequest: LoginRequest = {email: '', password: ''};
  // errorMessage: Array<String> = [];

  constructor(
    private keycloakService: KeycloakService,
  ) {
  }

  async ngOnInit(): Promise<void> {
        await this.keycloakService.init();
        await this.keycloakService.login();
    }

  // login() {
  //   this.errorMessage = [];
  //   this.authenticationService.login({
  //     body: this.loginRequest
  //   }).subscribe({
  //     next: (res) => {
  //       this.tokenService.token = res.token as string;
  //       this.router.navigate(['']);
  //     },
  //     error: (error) => {
  //       console.log(error);
  //       if (error.error.validationErrors) {
  //         this.errorMessage = error.error.validationErrors;
  //       } else {
  //         this.errorMessage.push(error.error.error);
  //       }
  //     }
  //   });
  // }

}

