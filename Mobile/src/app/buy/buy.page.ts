import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Product} from '../../models/product';
import {OrderService} from '../../services/order.service';
import {Order} from '../../models/order';
import {LoginService} from '../../services/login.service';

@Component({
  selector: 'app-buy',
  templateUrl: './buy.page.html',
  styleUrls: ['./buy.page.scss'],
})
export class BuyPage implements OnInit {

  cart: Array<Product>;

  constructor(
      private route: ActivatedRoute,
      private router: Router,
      private orderService: OrderService,
      private loginService: LoginService
  ) {
    this.route.queryParams.subscribe(_ => {
      if (this.router.getCurrentNavigation().extras.state) {
        this.cart = this.router.getCurrentNavigation().extras.state.cart;
        console.log(this.cart);
      }
    });
  }

  ngOnInit() {
  }

  getPrice(): number {
    let price = 0;

    for (const prod of this.cart) {
      price += prod.price;
    }

    return price;
  }

  makeBuyRequest() {
    const order = new Order({
      id: 0,
      user: this.loginService.getLoggedInUser(),
      totalPrice: this.getPrice(),
      products: this.cart
    });

    this.orderService.sendOrder(order);
  }
}
