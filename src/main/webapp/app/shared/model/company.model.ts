import { INotificator } from 'app/shared/model//notificator.model';
import { ICostumer } from 'app/shared/model//costumer.model';
import { IInvoice } from 'app/shared/model//invoice.model';
import { IBudget } from 'app/shared/model//budget.model';
import { IReport } from 'app/shared/model//report.model';

export interface ICompany {
    id?: number;
    title?: string;
    cId?: string;
    mainPhoneNumber?: string;
    secondaryPhoneNumber?: string;
    email?: string;
    address?: string;
    paypalAccount?: string;
    bankAccount?: string;
    logoContentType?: string;
    logo?: any;
    secondaryLogoContentType?: string;
    secondaryLogo?: any;
    stampContentType?: string;
    stamp?: any;
    notificators?: INotificator[];
    costumers?: ICostumer[];
    invoices?: IInvoice[];
    budgets?: IBudget[];
    reports?: IReport[];
}

export class Company implements ICompany {
    constructor(
        public id?: number,
        public title?: string,
        public cId?: string,
        public mainPhoneNumber?: string,
        public secondaryPhoneNumber?: string,
        public email?: string,
        public address?: string,
        public paypalAccount?: string,
        public bankAccount?: string,
        public logoContentType?: string,
        public logo?: any,
        public secondaryLogoContentType?: string,
        public secondaryLogo?: any,
        public stampContentType?: string,
        public stamp?: any,
        public notificators?: INotificator[],
        public costumers?: ICostumer[],
        public invoices?: IInvoice[],
        public budgets?: IBudget[],
        public reports?: IReport[]
    ) {}
}
