import { Injectable } from '@angular/core';
import {Product} from '../models/product';
import {Order} from '../models/order';
import {RequestsService} from './requests.service';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  constructor(
      private request: RequestsService
  ) { }

  // TODO placeholder implementation, get orders lists from the actual servers
  // getOrders(username: string): Order[] {
  //   return [new Order({
  //     id: 5,
  //     user: username,
  //     totalPrice: 1500,
  //     products: [new Product({
  //       id: 1,
  //       name: 'Flour',
  //       category: 'Food',
  //       stock: 5,
  //       price: 10,
  //       country: 'Romania'
  //     }), new Product({
  //       id: 2,
  //       name: 'Rice',
  //       category: 'Food',
  //       stock: 3,
  //       price: 8,
  //       country: 'Romania'
  //     }), new Product({
  //       id: 3,
  //       name: 'T-Shirt',
  //       category: 'Clothes',
  //       stock: 10,
  //       price: 50,
  //       country: 'Romania'
  //     })]}
  //   )];
  // }

  sendOrder(order: Order) {
    this.request.postOrder(order)
        .then(_ => {
          console.log('Posted order');
          return true;
        })
        .catch((error) => {
          console.log(error);
          return false;
        });
  }

  getOrders(username: string): Order[] {
    const toReturn = new Array<Order>();

    this.request.getOrders(username)
        .then(response => {

          if (response) {
            for (const order of response) {

              const orderProducts = new Array<Product>();
              for (const product of order.products) {
                orderProducts.push(new Product({
                  id: product.id,
                  name: product.name,
                  category: product.category,
                  stock: product.stock,
                  price: product.price,
                  country: product.country
                }));
              }

              toReturn.push(new Order({
                id: order.id,
                user: order.user,
                totalPrice: order.totalPrice,
                products: orderProducts
              }));
            }
          }

        })
        .catch((error) => console.log(error));

    return toReturn;
  }
}
