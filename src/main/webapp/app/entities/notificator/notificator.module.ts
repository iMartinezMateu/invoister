import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InvoisterSharedModule } from 'app/shared';
import {
    NotificatorComponent,
    NotificatorDetailComponent,
    NotificatorUpdateComponent,
    NotificatorDeletePopupComponent,
    NotificatorDeleteDialogComponent,
    notificatorRoute,
    notificatorPopupRoute
} from './';

const ENTITY_STATES = [...notificatorRoute, ...notificatorPopupRoute];

@NgModule({
    imports: [InvoisterSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        NotificatorComponent,
        NotificatorDetailComponent,
        NotificatorUpdateComponent,
        NotificatorDeleteDialogComponent,
        NotificatorDeletePopupComponent
    ],
    entryComponents: [NotificatorComponent, NotificatorUpdateComponent, NotificatorDeleteDialogComponent, NotificatorDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InvoisterNotificatorModule {}
