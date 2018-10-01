/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { InvoisterTestModule } from '../../../test.module';
import { NotificatorUpdateComponent } from 'app/entities/notificator/notificator-update.component';
import { NotificatorService } from 'app/entities/notificator/notificator.service';
import { Notificator } from 'app/shared/model/notificator.model';

describe('Component Tests', () => {
    describe('Notificator Management Update Component', () => {
        let comp: NotificatorUpdateComponent;
        let fixture: ComponentFixture<NotificatorUpdateComponent>;
        let service: NotificatorService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InvoisterTestModule],
                declarations: [NotificatorUpdateComponent]
            })
                .overrideTemplate(NotificatorUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(NotificatorUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NotificatorService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Notificator(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.notificator = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Notificator();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.notificator = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
