/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { InvoisterTestModule } from '../../../test.module';
import { NotificatorComponent } from 'app/entities/notificator/notificator.component';
import { NotificatorService } from 'app/entities/notificator/notificator.service';
import { Notificator } from 'app/shared/model/notificator.model';

describe('Component Tests', () => {
    describe('Notificator Management Component', () => {
        let comp: NotificatorComponent;
        let fixture: ComponentFixture<NotificatorComponent>;
        let service: NotificatorService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InvoisterTestModule],
                declarations: [NotificatorComponent],
                providers: []
            })
                .overrideTemplate(NotificatorComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(NotificatorComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NotificatorService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Notificator(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.notificators[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
