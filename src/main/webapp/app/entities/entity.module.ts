import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { InvoisterCompanyModule } from './company/company.module';
import { InvoisterCostumerModule } from './costumer/costumer.module';
import { InvoisterInvoiceModule } from './invoice/invoice.module';
import { InvoisterReportModule } from './report/report.module';
import { InvoisterBudgetModule } from './budget/budget.module';
import { InvoisterNotificatorModule } from './notificator/notificator.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        InvoisterCompanyModule,
        InvoisterCostumerModule,
        InvoisterInvoiceModule,
        InvoisterReportModule,
        InvoisterBudgetModule,
        InvoisterNotificatorModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InvoisterEntityModule {}
