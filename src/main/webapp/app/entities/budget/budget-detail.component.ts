import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IBudget } from 'app/shared/model/budget.model';

@Component({
    selector: 'jhi-budget-detail',
    templateUrl: './budget-detail.component.html'
})
export class BudgetDetailComponent implements OnInit {
    budget: IBudget;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ budget }) => {
            this.budget = budget;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }
}
