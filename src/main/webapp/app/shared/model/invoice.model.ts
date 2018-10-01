import { Moment } from 'moment';
import { IInvoiceItem } from 'app/shared/model//invoice-item.model';

export const enum PaymentForm {
    PAYPAL = 'PAYPAL',
    BANKTRANSFER = 'BANKTRANSFER',
    OTHER = 'OTHER'
}

export const enum InvoiceStatus {
    PAID = 'PAID',
    ISSUED = 'ISSUED',
    CANCELLED = 'CANCELLED'
}

export interface IInvoice {
    id?: number;
    date?: Moment;
    tax?: number;
    paymentForm?: PaymentForm;
    paymentDetails?: string;
    paymentDate?: Moment;
    status?: InvoiceStatus;
    companyId?: number;
    invoiceItems?: IInvoiceItem[];
}

export class Invoice implements IInvoice {
    constructor(
        public id?: number,
        public date?: Moment,
        public tax?: number,
        public paymentForm?: PaymentForm,
        public paymentDetails?: string,
        public paymentDate?: Moment,
        public status?: InvoiceStatus,
        public companyId?: number,
        public invoiceItems?: IInvoiceItem[]
    ) {}
}
