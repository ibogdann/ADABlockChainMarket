import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Order} from '../models/order';

@Injectable({
  providedIn: 'root'
})
export class RequestsService {

  private url: string = 'https://block-chain-market.herokuapp.com';

  constructor(
      private http: HttpClient
  ) { }

  postLogin(username: string, password: string): Promise<any> {
    return this.http.post(this.url + '/login', {
      username: username,
      password: password
    }).toPromise();
  }

  postLogout(username: string): Promise<any> {
    return this.http.post(this.url + '/logout', {
      username: username
    }).toPromise();
  }

  getOrders(username: string): Promise<any> {
    return this.http.get(this.url + '/orders?user=' + username).toPromise();
  }

  getProducts(locale: string): Promise<any> {
    return this.http.get(this.url + '/products?locale=' + locale).toPromise();
  }

  getProduct(productId: number): Promise<any> {
    return this.http.get(this.url + '/products/' + productId).toPromise();
  }

  postOrder(productIds, quantities): Promise<any> {
    return this.http.post(this.url + '/orders', {
      productsId: productIds,
      productsQuantity: quantities
    }).toPromise();
  }

}
