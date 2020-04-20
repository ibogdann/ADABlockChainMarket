import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Order} from '../models/order';

@Injectable({
  providedIn: 'root'
})
export class RequestsService {

  private url: string = 'http://localhost:5000';

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

  postOrder(order: Order): Promise<any> {
    return this.http.post(this.url + '/order', {
      order: order
    }).toPromise();
  }

}
