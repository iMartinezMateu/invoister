import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { INotificator } from 'app/shared/model/notificator.model';

type EntityResponseType = HttpResponse<INotificator>;
type EntityArrayResponseType = HttpResponse<INotificator[]>;

@Injectable({ providedIn: 'root' })
export class NotificatorService {
    private resourceUrl = SERVER_API_URL + 'api/notificators';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/notificators';

    constructor(private http: HttpClient) {}

    create(notificator: INotificator): Observable<EntityResponseType> {
        return this.http.post<INotificator>(this.resourceUrl, notificator, { observe: 'response' });
    }

    update(notificator: INotificator): Observable<EntityResponseType> {
        return this.http.put<INotificator>(this.resourceUrl, notificator, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<INotificator>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<INotificator[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<INotificator[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
