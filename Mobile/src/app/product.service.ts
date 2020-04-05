import {Injectable} from '@angular/core';
import {Product} from '../models/product';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor() { }

  // TODO placeholder implementation, get product lists from the actual servers
  getProducts(locale): Product[] {
    switch (locale) {
      case 'ro': {
        return [new Product({
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
        })];
      }

      case 'de': {
        return [new Product({
          id: 1,
          name: 'Chocolate',
          category: 'Food',
          stock: 4,
          price: 5,
          country: 'Germany'
        }), new Product({
          id: 2,
          name: 'Jeans',
          category: 'Clothes',
          stock: 2,
          price: 100,
          country: 'Germany'
        })];
      }

      case 'int': {
        return [new Product({
          id: 1,
          name: 'Monitor',
          category: 'Electronics',
          stock: 1,
          price: 1000,
          country: 'France'
        })];
      }

      default: {
        return [];
      }
    }
  }
}


