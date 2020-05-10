import { Component } from '@angular/core';
import {ProductService} from '../../services/product.service';
import {Product} from '../../models/product';
import {NavigationExtras, Router} from '@angular/router';
import {ToastController} from '@ionic/angular';
import {LoginService} from '../../services/login.service';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage {

  locale = 'ro';
  productList: Array<Product>;
  productsToOrder: Array<number> = new Array<number>();
  quantitiesToOrder: Array<number> = new Array<number>();
  cart: Array<Product> = new Array<Product>();

  constructor(
      private productService: ProductService,
      private router: Router,
      private toastController: ToastController,
      private loginService: LoginService
  ) {
    this.productList = this.productService.getProducts(this.locale);
  }

  refreshProducts() {
    this.productList = this.productService.getProducts(this.locale);
  }

  addProductQuantity(product: Product) {
    product.quantity++;
  }

  removeProductQuantity(product: Product) {
    if (product.quantity !== 0) {
      product.quantity--;
    }
  }

  async presentToast() {
    const toast = await this.toastController.create({
      message: 'No products have been added to cart!',
      duration: 3000
    });
    toast.present();
  }

  goToBuyPage() {
    for (const product of this.productList) {
      if (product.quantity && product.quantity > 0) {
        this.productsToOrder.push(product.id);
        this.quantitiesToOrder.push(product.quantity);
        this.cart.push(product);
      }
    }

    if (this.productsToOrder.length === 0) {
      this.presentToast();
    } else {
      const navigationExtras: NavigationExtras = {
        state: {
          productsToOrder: this.productsToOrder,
          quantitiesToOrder: this.quantitiesToOrder,
          cart: this.cart
        }
      };
      this.router.navigate(['buy'], navigationExtras);
    }
  }

  goToOrdersPage() {
    this.router.navigate(['orders']);
  }

  logout() {
    this.loginService.doLogout();
    this.router.navigate(['login']);
  }
}
