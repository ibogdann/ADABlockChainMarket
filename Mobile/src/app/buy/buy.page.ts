import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Product} from '../../models/product';
import {ProductService} from '../product.service';

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
      private productService: ProductService
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
    this.productService.sendCart(this.cart);
  }
}
