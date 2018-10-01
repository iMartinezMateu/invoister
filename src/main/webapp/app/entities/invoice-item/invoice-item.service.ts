import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IInvoiceItem } from 'app/shared/model/invoice-item.model';

type EntityResponseType = HttpResponse<IInvoiceItem>;
type EntityArrayResponseType = HttpResponse<IInvoiceItem[]>;

@Injectable({ providedIn: 'root' })
export class InvoiceItemService {
    private resourceUrl = SERVER_API_URL + 'api/invoice-items';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/invoice-items';

    constructor(private http: HttpClient) {}

    create(invoiceItem: IInvoiceItem): Observable<EntityResponseType> {
        return this.http.post<IInvoiceItem>(this.resourceUrl, invoiceItem, { observe: 'response' });
    }

    update(invoiceItem: IInvoiceItem): Observable<EntityResponseType> {
        return this.http.put<IInvoiceItem>(this.resourceUrl, invoiceItem, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IInvoiceItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IInvoiceItem[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IInvoiceItem[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
