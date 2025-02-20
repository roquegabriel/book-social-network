import { Injectable } from '@angular/core';
import { JwtHelperService } from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  isTokenInvalid(): boolean {
    return !this.isTokenValid();
  }
  isTokenValid(): boolean {
    const token = this.token;
    if (!token) {
      return false;
    }
    //decode the token
    const jwtHelper = new JwtHelperService();

    //check expiry date
    const isTokenExpired = jwtHelper.isTokenExpired(token);

    if (isTokenExpired) {
      localStorage.clear();
      return false;
    }
    return true;
  }

  set token(token: string) {
    localStorage.setItem('token', token);
  }

  get token(): string {
    return localStorage.getItem('token') as string;
  }

}
