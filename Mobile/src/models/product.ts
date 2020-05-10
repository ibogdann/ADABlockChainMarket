export class Product {
    id: number;
    name: string;
    category: string;
    stock: number;
    price: number;
    country: string;
    quantity: number = 0;

    constructor({id, name, category, stock, price, country}) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.stock = stock;
        this.price = price;
        this.country = country;
    }

}
