import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IBudget } from 'app/shared/model/budget.model';
import { BudgetService } from './budget.service';
import { ICompany } from 'app/shared/model/company.model';
import { CompanyService } from 'app/entities/company';

@Component({
    selector: 'jhi-budget-update',
    templateUrl: './budget-update.component.html'
})
export class BudgetUpdateComponent implements OnInit {
    private _budget: IBudget;
    isSaving: boolean;

    companies: ICompany[];
    dateDp: any;

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private budgetService: BudgetService,
        private companyService: CompanyService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ budget }) => {
            this.budget = budget;
        });
        this.companyService.query().subscribe(
            (res: HttpResponse<ICompany[]>) => {
                this.companies = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.budget.id !== undefined) {
            this.subscribeToSaveResponse(this.budgetService.update(this.budget));
        } else {
            this.subscribeToSaveResponse(this.budgetService.create(this.budget));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IBudget>>) {
        result.subscribe((res: HttpResponse<IBudget>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get budget() {
        return this._budget;
    }

    set budget(budget: IBudget) {
        this._budget = budget;
    }
}
