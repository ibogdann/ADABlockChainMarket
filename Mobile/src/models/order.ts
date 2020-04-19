import {Product} from './product';

export class Order {
    id: number;
    totalPrice: number;
    products: Array<Product>;

    constructor({id, totalPrice, products}) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.products = products;
    }
}
