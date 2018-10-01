export interface ICostumer {
    id?: number;
    name?: string;
    cId?: string;
    address?: string;
    phone?: string;
    email?: string;
    companyId?: number;
}

export class Costumer implements ICostumer {
    constructor(
        public id?: number,
        public name?: string,
        public cId?: string,
        public address?: string,
        public phone?: string,
        public email?: string,
        public companyId?: number
    ) {}
}
