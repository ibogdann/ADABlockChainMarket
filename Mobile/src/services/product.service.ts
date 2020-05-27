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

  getProducts(locale): Product[] {
    const toReturn = new Array<Product>();

    this.request.getProducts(locale)
        .then(response => {

          console.log(response);
          if (response) {
            for (const product of response._embedded.productList) {
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


