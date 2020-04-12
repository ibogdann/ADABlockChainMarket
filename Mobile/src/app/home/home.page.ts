import { Component } from '@angular/core';
import {ProductService} from '../product.service';
import {Product} from '../../models/product';
import {NavigationExtras, Router} from '@angular/router';
import {ToastController} from '@ionic/angular';

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
      private productService: ProductService,
      private router: Router,
      private toastController: ToastController
  ) {
    this.productList = this.productService.getProducts(this.locale);
  }

  refreshProducts() {
    this.productList = this.productService.getProducts(this.locale);
  }

  isProductAlreadyInCart(product): boolean {
    for (const prod of this.cart) {
      if (prod.id === product.id) {
        return true;
      }
    }
    return false;
  }

  getButtonIcon(product): string {
    if (this.isProductAlreadyInCart(product)) {
      return 'close-outline';
    } else {
      return 'add-outline';
    }
  }

  getButtonColor(product): string {
    if (this.isProductAlreadyInCart(product)) {
      return 'danger';
    } else {
      return 'primary';
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

    // for (const prod of this.cart) {
    //   console.log(prod);
    // }
  }

  async presentToast() {
    const toast = await this.toastController.create({
      message: 'No products have been added to cart!',
      duration: 3000
    });
    toast.present();
  }

  goToBuyPage() {
    if (this.cart.length === 0) {
      this.presentToast();
    } else {
      const navigationExtras: NavigationExtras = {
        state: {
          cart: this.cart
        }
      };
      this.router.navigate(['buy'], navigationExtras);
    }
  }
}
