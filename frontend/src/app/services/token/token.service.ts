import {Injectable} from '@angular/core';
import {JwtHelperService} from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  constructor() {
  }

  isTokenNotValid() {
    return !this.isTokenValid();
  }

  isTokenValid() {
    const token = this.token;
    if (!token) {
      return false;
    }
    // decode token
    const jwtHelper = new JwtHelperService();
    // check if token is expired
    const isTokenExpired = jwtHelper.isTokenExpired(token);
    if (isTokenExpired) {
      sessionStorage.clear();
      return false;
    }
    return true;
  }


  set token(token: string) {
    sessionStorage.setItem("token", token);
  }

  get token() {
    return sessionStorage.getItem("token") as string;
  }

}
