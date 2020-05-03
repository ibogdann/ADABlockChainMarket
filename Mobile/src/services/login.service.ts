import { Injectable } from '@angular/core';
import {RequestsService} from './requests.service';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  loggedInUser: string = '';

  constructor(
      private request: RequestsService
  ) { }

  doLogin(username: string, password: string): boolean {

    if (username === 'abcd' && password === '1234') {
      this.loggedInUser = username;
      return true;
    }

    return false;

    // this.request.postLogin(username, password)
    //     .then(_ => {
    //       console.log('Logged in user ' + username + ' with password ' + password);
    //       return true;
    //     })
    //     .catch((error) => {
    //       console.log(error);
    //       return false;
    //     });
    // return false;
  }

  doLogout() {
    this.loggedInUser = '';

    // this.request.postLogout(this.loggedInUser)
    //     .then(_ => {
    //       console.log('Logged out user ' + this.loggedInUser);
    //       return true;
    //     })
    //     .catch((error) => {
    //       console.log(error);
    //       return false;
    //     });
  }

  getLoggedInUser(): string {
      return this.loggedInUser;
  }

}
