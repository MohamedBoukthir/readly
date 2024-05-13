import {Component} from '@angular/core';
import {AuthenticationService} from "../../services/services/authentication.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-activate-account',
  templateUrl: './activate-account.component.html',
  styleUrl: './activate-account.component.css'
})
export class ActivateAccountComponent {

  message = '';
  isOkay = false;
  submitted = false;

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService
  ) {
  }

  onCodeCompleted(code: string) {
    this.confirmAccount(code);
  }

  private confirmAccount(token: string) {
    this.authenticationService.confirm({
      token
    }).subscribe({
      next: () => {
        this.message = 'Account activated successfully';
        this.submitted = true;
        this.isOkay = true;
      },
      error: error => {
        this.message = 'Token is invalid or expired';
        this.submitted = true;
        this.isOkay = false;
      }
    })
  }
}
