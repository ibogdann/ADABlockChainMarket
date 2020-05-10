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

  productsToOrder: Array<number>;
  quantitiesToOrder: Array<number>;
  cart: Array<Product>;

  constructor(
      private route: ActivatedRoute,
      private router: Router,
      private orderService: OrderService,
      private loginService: LoginService
  ) {
    this.route.queryParams.subscribe(_ => {
      if (this.router.getCurrentNavigation().extras.state) {
        this.productsToOrder = this.router.getCurrentNavigation().extras.state.productsToOrder;
        this.quantitiesToOrder = this.router.getCurrentNavigation().extras.state.quantitiesToOrder;
        this.cart = this.router.getCurrentNavigation().extras.state.cart;

        console.log(this.productsToOrder);
        console.log(this.quantitiesToOrder);
      }
    });
  }

  getPrice() {
    let price = 0;
    for (const product of this.cart) {
      price += product.quantity * product.price;
    }

    return price;
  }

  ngOnInit() {
  }

  makeBuyRequest() {
    this.orderService.sendOrder(this.productsToOrder, this.quantitiesToOrder);
  }
}
