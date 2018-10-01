import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { InvoiceItem } from 'app/shared/model/invoice-item.model';
import { InvoiceItemService } from './invoice-item.service';
import { InvoiceItemComponent } from './invoice-item.component';
import { InvoiceItemDetailComponent } from './invoice-item-detail.component';
import { InvoiceItemUpdateComponent } from './invoice-item-update.component';
import { InvoiceItemDeletePopupComponent } from './invoice-item-delete-dialog.component';
import { IInvoiceItem } from 'app/shared/model/invoice-item.model';

@Injectable({ providedIn: 'root' })
export class InvoiceItemResolve implements Resolve<IInvoiceItem> {
    constructor(private service: InvoiceItemService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((invoiceItem: HttpResponse<InvoiceItem>) => invoiceItem.body));
        }
        return of(new InvoiceItem());
    }
}

export const invoiceItemRoute: Routes = [
    {
        path: 'invoice-item',
        component: InvoiceItemComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'invoisterApp.invoiceItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'invoice-item/:id/view',
        component: InvoiceItemDetailComponent,
        resolve: {
            invoiceItem: InvoiceItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'invoisterApp.invoiceItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'invoice-item/new',
        component: InvoiceItemUpdateComponent,
        resolve: {
            invoiceItem: InvoiceItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'invoisterApp.invoiceItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'invoice-item/:id/edit',
        component: InvoiceItemUpdateComponent,
        resolve: {
            invoiceItem: InvoiceItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'invoisterApp.invoiceItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const invoiceItemPopupRoute: Routes = [
    {
        path: 'invoice-item/:id/delete',
        component: InvoiceItemDeletePopupComponent,
        resolve: {
            invoiceItem: InvoiceItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'invoisterApp.invoiceItem.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
