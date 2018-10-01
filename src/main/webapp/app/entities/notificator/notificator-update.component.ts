import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { INotificator } from 'app/shared/model/notificator.model';
import { NotificatorService } from './notificator.service';
import { ICompany } from 'app/shared/model/company.model';
import { CompanyService } from 'app/entities/company';

@Component({
    selector: 'jhi-notificator-update',
    templateUrl: './notificator-update.component.html'
})
export class NotificatorUpdateComponent implements OnInit {
    private _notificator: INotificator;
    isSaving: boolean;

    companies: ICompany[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private notificatorService: NotificatorService,
        private companyService: CompanyService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ notificator }) => {
            this.notificator = notificator;
        });
        this.companyService.query().subscribe(
            (res: HttpResponse<ICompany[]>) => {
                this.companies = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.notificator.id !== undefined) {
            this.subscribeToSaveResponse(this.notificatorService.update(this.notificator));
        } else {
            this.subscribeToSaveResponse(this.notificatorService.create(this.notificator));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<INotificator>>) {
        result.subscribe((res: HttpResponse<INotificator>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackCompanyById(index: number, item: ICompany) {
        return item.id;
    }
    get notificator() {
        return this._notificator;
    }

    set notificator(notificator: INotificator) {
        this._notificator = notificator;
    }
}
