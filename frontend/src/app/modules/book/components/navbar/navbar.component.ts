import { Component } from '@angular/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  menuHidden = true;
  currentYear = new Date().getFullYear();

  toggleMenu(): void {
    this.menuHidden = !this.menuHidden;
  }

  logout() {

  }
}
