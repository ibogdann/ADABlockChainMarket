import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Order} from '../models/order';

@Injectable({
  providedIn: 'root'
})
export class RequestsService {

  public servers: string[] = ['https://block-chain-market.herokuapp.com', 'server2'];
  public url: string = this.servers[0];

  constructor(
      private http: HttpClient
  ) { }

  postLogin(username: string, password: string): Promise<any> {
    return this.http.post(this.url + '/login', {
      username: username,
      password: password
    }).toPromise();
  }

  getOrders(username: string): Promise<any> {
    return this.http.get(this.url + '/orders?user=' + username).toPromise();
  }

  getProducts(): Promise<any> {
    return this.http.get(this.url + '/products').toPromise();
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
