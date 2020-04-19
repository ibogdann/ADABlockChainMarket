import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  loggedInUser: string = '';

  constructor() { }

  doLogin(username: string, password: string): boolean {

    if (username === 'abcd' && password === '1234') {
      this.loggedInUser = username;
      return true;
    }

    return false;
  }

  doLogout() {
    this.loggedInUser = '';
  }

}
