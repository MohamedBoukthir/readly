import { Component } from '@angular/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  menuHidden = true;

  toggleMenu(): void {
    this.menuHidden = !this.menuHidden;
  }

}
