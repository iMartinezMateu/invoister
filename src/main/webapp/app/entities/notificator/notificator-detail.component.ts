import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INotificator } from 'app/shared/model/notificator.model';

@Component({
    selector: 'jhi-notificator-detail',
    templateUrl: './notificator-detail.component.html'
})
export class NotificatorDetailComponent implements OnInit {
    notificator: INotificator;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ notificator }) => {
            this.notificator = notificator;
        });
    }

    previousState() {
        window.history.back();
    }
}
