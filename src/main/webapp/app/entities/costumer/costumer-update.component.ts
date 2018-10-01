import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ICostumer } from 'app/shared/model/costumer.model';
import { CostumerService } from './costumer.service';
import { ICompany } from 'app/shared/model/company.model';
import { CompanyService } from 'app/entities/company';

@Component({
    selector: 'jhi-costumer-update',
    templateUrl: './costumer-update.component.html'
})
export class CostumerUpdateComponent implements OnInit {
    private _costumer: ICostumer;
    isSaving: boolean;

    companies: ICompany[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private costumerService: CostumerService,
        private companyService: CompanyService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ costumer }) => {
            this.costumer = costumer;
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
        if (this.costumer.id !== undefined) {
            this.subscribeToSaveResponse(this.costumerService.update(this.costumer));
        } else {
            this.subscribeToSaveResponse(this.costumerService.create(this.costumer));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICostumer>>) {
        result.subscribe((res: HttpResponse<ICostumer>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get costumer() {
        return this._costumer;
    }

    set costumer(costumer: ICostumer) {
        this._costumer = costumer;
    }
}
