import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Invoice } from 'app/shared/model/invoice.model';
import { InvoiceService } from './invoice.service';
import { InvoiceComponent } from './invoice.component';
import { InvoiceDetailComponent } from './invoice-detail.component';
import { InvoiceUpdateComponent } from './invoice-update.component';
import { InvoiceDeletePopupComponent } from './invoice-delete-dialog.component';
import { IInvoice } from 'app/shared/model/invoice.model';

@Injectable({ providedIn: 'root' })
export class InvoiceResolve implements Resolve<IInvoice> {
    constructor(private service: InvoiceService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((invoice: HttpResponse<Invoice>) => invoice.body));
        }
        return of(new Invoice());
    }
}

export const invoiceRoute: Routes = [
    {
        path: 'invoice',
        component: InvoiceComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'invoisterApp.invoice.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'invoice/:id/view',
        component: InvoiceDetailComponent,
        resolve: {
            invoice: InvoiceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'invoisterApp.invoice.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'invoice/new',
        component: InvoiceUpdateComponent,
        resolve: {
            invoice: InvoiceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'invoisterApp.invoice.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'invoice/:id/edit',
        component: InvoiceUpdateComponent,
        resolve: {
            invoice: InvoiceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'invoisterApp.invoice.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const invoicePopupRoute: Routes = [
    {
        path: 'invoice/:id/delete',
        component: InvoiceDeletePopupComponent,
        resolve: {
            invoice: InvoiceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'invoisterApp.invoice.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
