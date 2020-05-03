import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {LoginService} from '../../services/login.service';
import {OrderService} from '../../services/order.service';
import {Order} from '../../models/order';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.page.html',
  styleUrls: ['./orders.page.scss'],
})
export class OrdersPage implements OnInit {

  orderList: Array<Order>;

  constructor(
      private router: Router,
      private loginService: LoginService,
      private orderService: OrderService
  ) {
    this.orderList = this.orderService.getOrders(this.loginService.getLoggedInUser());
  }

  ngOnInit() {
  }

  goToProductsPage() {
    this.router.navigate(['home']);
  }

  logout() {
    this.loginService.doLogout();
    this.router.navigate(['login']);
  }
}
