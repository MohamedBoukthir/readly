import { Injectable } from '@angular/core';
import Keycloak from "keycloak-js";
import {UserProfile} from "../token/userProfile";

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {

  private _keycloak: Keycloak | undefined;
  private _profile: UserProfile | undefined;

  get keycloak() {
    if (!this._keycloak) {
      this._keycloak = new Keycloak({
        url: 'http://localhost:9090',
        realm: 'readly',
        clientId:'readly'
      });
    }
    return this._keycloak;
  }

  get userProfile(): UserProfile | undefined {
    return this._profile;
  }

  constructor() { }

  async init() {
    const authenticated = await this.keycloak?.init({
      onLoad: 'login-required'
    });
    if (authenticated) {
      this._profile = (await this.keycloak.loadUserProfile()) as UserProfile;
      this._profile.token = this.keycloak.token;
    }
  }

  login() {
    return  this.keycloak?.login();
  }

  logout() {
    // return this.keycloak?.accountManagement();
    return this.keycloak?.logout();
  }
}
