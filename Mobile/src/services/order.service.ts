import { Injectable } from '@angular/core';
import {Product} from '../models/product';
import {Order} from '../models/order';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  constructor() { }

  // TODO placeholder implementation, get orders lists from the actual servers
  getOrders(): Order[] {
    return [new Order({
      id: 5,
      totalPrice: 1500,
      products: [new Product({
        id: 1,
        name: 'Flour',
        category: 'Food',
        stock: 5,
        price: 10,
        country: 'Romania'
      }), new Product({
        id: 2,
        name: 'Rice',
        category: 'Food',
        stock: 3,
        price: 8,
        country: 'Romania'
      }), new Product({
        id: 3,
        name: 'T-Shirt',
        category: 'Clothes',
        stock: 10,
        price: 50,
        country: 'Romania'
      })]}
    )];
  }
}
