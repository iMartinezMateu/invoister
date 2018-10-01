import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InvoisterSharedModule } from 'app/shared';
import {
    CostumerComponent,
    CostumerDetailComponent,
    CostumerUpdateComponent,
    CostumerDeletePopupComponent,
    CostumerDeleteDialogComponent,
    costumerRoute,
    costumerPopupRoute
} from './';

const ENTITY_STATES = [...costumerRoute, ...costumerPopupRoute];

@NgModule({
    imports: [InvoisterSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CostumerComponent,
        CostumerDetailComponent,
        CostumerUpdateComponent,
        CostumerDeleteDialogComponent,
        CostumerDeletePopupComponent
    ],
    entryComponents: [CostumerComponent, CostumerUpdateComponent, CostumerDeleteDialogComponent, CostumerDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InvoisterCostumerModule {}
