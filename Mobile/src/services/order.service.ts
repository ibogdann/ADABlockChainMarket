/* tslint:disable:no-shadowed-variable */
import { Injectable } from '@angular/core';
import {Product} from '../models/product';
import {Order} from '../models/order';
import {RequestsService} from './requests.service';
import {delay} from 'rxjs/operators';
import {LoginService} from './login.service';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  constructor(
      private request: RequestsService,
      private loginService: LoginService
  ) { }

  sendOrder(productsToOrder: Array<number>, quantitiesToOrder: Array<number>) {
    this.request.postOrder(productsToOrder, quantitiesToOrder);
  }

  getOrders(username: string): Order[] {
    const toReturn = new Array<Order>();

    this.request.getOrders(username)
        .then(async response => {

          console.log(response);
          if (response) {
            for (const order of response._embedded.orderList) {

              const orderProducts = new Array<Product>();

              for (const productId of order.productsId) {

                const index = order.productsId.indexOf(productId);
                const qty = order.productsQuantity[index];

                await this.request.getProduct(productId).then(response => {
                  if (response) {
                    const product = new Product({
                      id: response.id,
                      name: response.name,
                      category: response.category,
                      stock: response.stock,
                      price: response.price,
                      country: response.country
                    });

                    product.quantity = qty;

                    orderProducts.push(product);
                  }
                });
              }

              console.log('## order products');
              console.log(orderProducts);

              toReturn.push(new Order({
                id: order.id,
                user: this.loginService.getLoggedInUser(),
                totalPrice: order.totalValue,
                products: orderProducts
              }));
            }
          }

        })
        .catch((error) => console.log(error));

    return toReturn;
  }
}
