import { Component, OnInit } from '@angular/core';
import {LoginService} from '../../services/login.service';
import {ToastController} from '@ionic/angular';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
})
export class LoginPage implements OnInit {

  username: string = '';
  password: string = '';

  constructor(
      private loginService: LoginService,
      private toastController: ToastController,
      private router: Router
  ) { }

  ngOnInit() {
  }

  login() {
    console.log(this.username + ' ' + this.password);

    this.loginService.doLogin(this.username, this.password);

    if (this.loginService.loggedInUser !== '') {
      this.router.navigate(['home']);
    } else {
      this.presentToast();
    }
  }

  async presentToast() {
    const toast = await this.toastController.create({
      message: 'Invalid credentials!',
      duration: 3000
    });
    toast.present();
  }

}
