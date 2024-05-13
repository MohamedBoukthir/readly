import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  constructor() { }

  set token(token: string) {
    sessionStorage.setItem("token", token);
  }

  get token() {
    return sessionStorage.getItem("token") as string;
  }

}