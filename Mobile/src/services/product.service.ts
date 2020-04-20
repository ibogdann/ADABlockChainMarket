import {Injectable} from '@angular/core';
import {Product} from '../models/product';
import {RequestsService} from './requests.service';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(
      private request: RequestsService
  ) { }

  // TODO placeholder implementation, get product lists from the actual servers
  // getProducts(locale): Product[] {
  //   switch (locale) {
  //     case 'ro': {
  //       return [new Product({
  //         id: 1,
  //         name: 'Flour',
  //         category: 'Food',
  //         stock: 5,
  //         price: 10,
  //         country: 'Romania'
  //       }), new Product({
  //         id: 2,
  //         name: 'Rice',
  //         category: 'Food',
  //         stock: 3,
  //         price: 8,
  //         country: 'Romania'
  //       }), new Product({
  //         id: 3,
  //         name: 'T-Shirt',
  //         category: 'Clothes',
  //         stock: 10,
  //         price: 50,
  //         country: 'Romania'
  //       })];
  //     }
  //
  //     case 'de': {
  //       return [new Product({
  //         id: 4,
  //         name: 'Chocolate',
  //         category: 'Food',
  //         stock: 4,
  //         price: 5,
  //         country: 'Germany'
  //       }), new Product({
  //         id: 5,
  //         name: 'Jeans',
  //         category: 'Clothes',
  //         stock: 2,
  //         price: 100,
  //         country: 'Germany'
  //       })];
  //     }
  //
  //     case 'int': {
  //       return [new Product({
  //         id: 6,
  //         name: 'Monitor',
  //         category: 'Electronics',
  //         stock: 1,
  //         price: 1000,
  //         country: 'France'
  //       })];
  //     }
  //
  //     default: {
  //       return [];
  //     }
  //   }
  // }

  getProducts(locale): Product[] {
    const toReturn = new Array<Product>();

    this.request.getProducts(locale)
        .then(response => {

          if (response) {
            for (const product of response) {
              toReturn.push(new Product({
                id: product.id,
                name: product.name,
                category: product.category,
                stock: product.stock,
                price: product.price,
                country: product.country
              }));
            }
          }

        })
        .catch((error) => console.log(error));

    return toReturn;
  }

}


