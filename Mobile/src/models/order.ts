import {Product} from './product';

export class Order {
    id: number;
    user: string;
    totalPrice: number;
    products: Array<Product>;

    constructor({id, user, totalPrice, products}) {
        this.id = id;
        this.user = user;
        this.totalPrice = totalPrice;
        this.products = products;
    }
}
