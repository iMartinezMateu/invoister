import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICostumer } from 'app/shared/model/costumer.model';

@Component({
    selector: 'jhi-costumer-detail',
    templateUrl: './costumer-detail.component.html'
})
export class CostumerDetailComponent implements OnInit {
    costumer: ICostumer;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ costumer }) => {
            this.costumer = costumer;
        });
    }

    previousState() {
        window.history.back();
    }
}
