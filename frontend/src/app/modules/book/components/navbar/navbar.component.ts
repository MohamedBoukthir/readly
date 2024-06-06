import {Component} from '@angular/core';
import {KeycloakService} from "../../../../services/keycloak/keycloak.service";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  constructor(
    private keycloakService: KeycloakService,
  ) {}

  menuHidden = true;
  currentYear = new Date().getFullYear();

  toggleMenu(): void {
    this.menuHidden = !this.menuHidden;
  }

  async logout() {
    this.keycloakService.logout();
  }
}

