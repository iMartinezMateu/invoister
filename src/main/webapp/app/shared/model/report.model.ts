import { Moment } from 'moment';

export interface IReport {
    id?: number;
    rId?: string;
    date?: Moment;
    body?: any;
    companyId?: number;
}

export class Report implements IReport {
    constructor(public id?: number, public rId?: string, public date?: Moment, public body?: any, public companyId?: number) {}
}
