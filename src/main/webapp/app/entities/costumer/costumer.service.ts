import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICostumer } from 'app/shared/model/costumer.model';

type EntityResponseType = HttpResponse<ICostumer>;
type EntityArrayResponseType = HttpResponse<ICostumer[]>;

@Injectable({ providedIn: 'root' })
export class CostumerService {
    private resourceUrl = SERVER_API_URL + 'api/costumers';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/costumers';

    constructor(private http: HttpClient) {}

    create(costumer: ICostumer): Observable<EntityResponseType> {
        return this.http.post<ICostumer>(this.resourceUrl, costumer, { observe: 'response' });
    }

    update(costumer: ICostumer): Observable<EntityResponseType> {
        return this.http.put<ICostumer>(this.resourceUrl, costumer, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICostumer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICostumer[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICostumer[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
