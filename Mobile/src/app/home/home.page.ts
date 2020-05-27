import { Component } from '@angular/core';
import {ProductService} from '../../services/product.service';
import {Product} from '../../models/product';
import {NavigationExtras, Router} from '@angular/router';
import {ToastController} from '@ionic/angular';
import {LoginService} from '../../services/login.service';
import {RequestsService} from '../../services/requests.service';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage {

  server = 'server1';
  productList: Array<Product>;
  productsToOrder: Array<number> = new Array<number>();
  quantitiesToOrder: Array<number> = new Array<number>();
  cart: Array<Product> = new Array<Product>();

  constructor(
      private productService: ProductService,
      private router: Router,
      private toastController: ToastController,
      private loginService: LoginService,
      private request: RequestsService
  ) {
    this.productList = this.productService.getProducts();
  }

  ionViewDidEnter() {
    this.cart = new Array<Product>();
    this.productsToOrder = new Array<number>();
    this.quantitiesToOrder = new Array<number>();
  }

  refreshProducts() {

    if (this.server === 'server1') {
      this.request.url = this.request.servers[0];
      console.log(this.request.url);
    } else if (this.server === 'server2') {
      this.request.url = this.request.servers[1];
      console.log(this.request.url);
    }

    this.productList = this.productService.getProducts();
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
