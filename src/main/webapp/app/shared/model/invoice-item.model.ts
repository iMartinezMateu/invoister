import { IInvoice } from 'app/shared/model//invoice.model';

export interface IInvoiceItem {
    id?: number;
    description?: string;
    quantity?: number;
    grossCost?: number;
    invoices?: IInvoice[];
}

export class InvoiceItem implements IInvoiceItem {
    constructor(
        public id?: number,
        public description?: string,
        public quantity?: number,
        public grossCost?: number,
        public invoices?: IInvoice[]
    ) {}
}
