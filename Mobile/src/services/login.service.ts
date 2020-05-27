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

  async doLogin(username: string, password: string) {
    await this.request.postLogin(username, password)
        .then(_ => {
          console.log('Logged in user ' + username + ' with password ' + password);
          this.loggedInUser = username;
        })
        .catch((error) => {
          console.log(error);
        });
  }

  doLogout() {
    this.loggedInUser = '';
  }

  getLoggedInUser(): string {
      return this.loggedInUser;
  }

}
