import { Component } from '@angular/core';
import {ProductService} from '../product.service';
import {Product} from '../../models/product';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage {

  locale = 'ro';
  productList: Array<Product>;
  cart: Array<Product> = new Array<Product>();

  constructor(
      private productService: ProductService
  ) {
    this.productList = this.productService.getProducts(this.locale);
  }

  refreshProducts() {
    this.productList = this.productService.getProducts(this.locale);
  }

  isProductAlreadyInCart(product): boolean {
    return this.cart.includes(product);
  }

  getButtonIcon(product): string {
    if (this.isProductAlreadyInCart(product)) {
      return 'close-outline';
    } else {
      return 'cart-outline';
    }
  }

  addToCart(productId) {
    const prodToAdd = this.productList.find(x => x.id === productId);

    if (!this.isProductAlreadyInCart(prodToAdd)) {
      // add to cart
      this.cart.push(this.productList.find(x => x.id === productId));
    } else {
      // remove from cart
      this.cart = this.cart.filter(x => x.id !== productId);
    }

    for (const prod of this.cart) {
      console.log(prod);
    }
  }
}
