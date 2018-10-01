import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Costumer } from 'app/shared/model/costumer.model';
import { CostumerService } from './costumer.service';
import { CostumerComponent } from './costumer.component';
import { CostumerDetailComponent } from './costumer-detail.component';
import { CostumerUpdateComponent } from './costumer-update.component';
import { CostumerDeletePopupComponent } from './costumer-delete-dialog.component';
import { ICostumer } from 'app/shared/model/costumer.model';

@Injectable({ providedIn: 'root' })
export class CostumerResolve implements Resolve<ICostumer> {
    constructor(private service: CostumerService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((costumer: HttpResponse<Costumer>) => costumer.body));
        }
        return of(new Costumer());
    }
}

export const costumerRoute: Routes = [
    {
        path: 'costumer',
        component: CostumerComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'invoisterApp.costumer.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'costumer/:id/view',
        component: CostumerDetailComponent,
        resolve: {
            costumer: CostumerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'invoisterApp.costumer.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'costumer/new',
        component: CostumerUpdateComponent,
        resolve: {
            costumer: CostumerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'invoisterApp.costumer.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'costumer/:id/edit',
        component: CostumerUpdateComponent,
        resolve: {
            costumer: CostumerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'invoisterApp.costumer.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const costumerPopupRoute: Routes = [
    {
        path: 'costumer/:id/delete',
        component: CostumerDeletePopupComponent,
        resolve: {
            costumer: CostumerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'invoisterApp.costumer.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
