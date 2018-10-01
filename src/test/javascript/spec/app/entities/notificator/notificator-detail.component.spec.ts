/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InvoisterTestModule } from '../../../test.module';
import { NotificatorDetailComponent } from 'app/entities/notificator/notificator-detail.component';
import { Notificator } from 'app/shared/model/notificator.model';

describe('Component Tests', () => {
    describe('Notificator Management Detail Component', () => {
        let comp: NotificatorDetailComponent;
        let fixture: ComponentFixture<NotificatorDetailComponent>;
        const route = ({ data: of({ notificator: new Notificator(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InvoisterTestModule],
                declarations: [NotificatorDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(NotificatorDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(NotificatorDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.notificator).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
