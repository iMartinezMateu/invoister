import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInvoiceItem } from 'app/shared/model/invoice-item.model';

@Component({
    selector: 'jhi-invoice-item-detail',
    templateUrl: './invoice-item-detail.component.html'
})
export class InvoiceItemDetailComponent implements OnInit {
    invoiceItem: IInvoiceItem;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ invoiceItem }) => {
            this.invoiceItem = invoiceItem;
        });
    }

    previousState() {
        window.history.back();
    }
}
