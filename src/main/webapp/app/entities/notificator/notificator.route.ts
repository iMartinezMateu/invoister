import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Notificator } from 'app/shared/model/notificator.model';
import { NotificatorService } from './notificator.service';
import { NotificatorComponent } from './notificator.component';
import { NotificatorDetailComponent } from './notificator-detail.component';
import { NotificatorUpdateComponent } from './notificator-update.component';
import { NotificatorDeletePopupComponent } from './notificator-delete-dialog.component';
import { INotificator } from 'app/shared/model/notificator.model';

@Injectable({ providedIn: 'root' })
export class NotificatorResolve implements Resolve<INotificator> {
    constructor(private service: NotificatorService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((notificator: HttpResponse<Notificator>) => notificator.body));
        }
        return of(new Notificator());
    }
}

export const notificatorRoute: Routes = [
    {
        path: 'notificator',
        component: NotificatorComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'invoisterApp.notificator.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'notificator/:id/view',
        component: NotificatorDetailComponent,
        resolve: {
            notificator: NotificatorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'invoisterApp.notificator.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'notificator/new',
        component: NotificatorUpdateComponent,
        resolve: {
            notificator: NotificatorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'invoisterApp.notificator.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'notificator/:id/edit',
        component: NotificatorUpdateComponent,
        resolve: {
            notificator: NotificatorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'invoisterApp.notificator.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const notificatorPopupRoute: Routes = [
    {
        path: 'notificator/:id/delete',
        component: NotificatorDeletePopupComponent,
        resolve: {
            notificator: NotificatorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'invoisterApp.notificator.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
