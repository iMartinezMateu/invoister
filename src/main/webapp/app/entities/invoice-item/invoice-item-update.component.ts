import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IInvoiceItem } from 'app/shared/model/invoice-item.model';
import { InvoiceItemService } from './invoice-item.service';
import { IInvoice } from 'app/shared/model/invoice.model';
import { InvoiceService } from 'app/entities/invoice';

@Component({
    selector: 'jhi-invoice-item-update',
    templateUrl: './invoice-item-update.component.html'
})
export class InvoiceItemUpdateComponent implements OnInit {
    private _invoiceItem: IInvoiceItem;
    isSaving: boolean;

    invoices: IInvoice[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private invoiceItemService: InvoiceItemService,
        private invoiceService: InvoiceService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ invoiceItem }) => {
            this.invoiceItem = invoiceItem;
        });
        this.invoiceService.query().subscribe(
            (res: HttpResponse<IInvoice[]>) => {
                this.invoices = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.invoiceItem.id !== undefined) {
            this.subscribeToSaveResponse(this.invoiceItemService.update(this.invoiceItem));
        } else {
            this.subscribeToSaveResponse(this.invoiceItemService.create(this.invoiceItem));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IInvoiceItem>>) {
        result.subscribe((res: HttpResponse<IInvoiceItem>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackInvoiceById(index: number, item: IInvoice) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
    get invoiceItem() {
        return this._invoiceItem;
    }

    set invoiceItem(invoiceItem: IInvoiceItem) {
        this._invoiceItem = invoiceItem;
    }
}
