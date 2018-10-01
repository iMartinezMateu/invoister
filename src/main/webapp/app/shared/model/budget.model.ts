import { Moment } from 'moment';

export interface IBudget {
    id?: number;
    date?: Moment;
    body?: any;
    grossCost?: number;
    tax?: number;
    companyId?: number;
}

export class Budget implements IBudget {
    constructor(
        public id?: number,
        public date?: Moment,
        public body?: any,
        public grossCost?: number,
        public tax?: number,
        public companyId?: number
    ) {}
}
