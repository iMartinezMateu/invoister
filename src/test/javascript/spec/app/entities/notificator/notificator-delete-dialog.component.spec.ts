/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { InvoisterTestModule } from '../../../test.module';
import { NotificatorDeleteDialogComponent } from 'app/entities/notificator/notificator-delete-dialog.component';
import { NotificatorService } from 'app/entities/notificator/notificator.service';

describe('Component Tests', () => {
    describe('Notificator Management Delete Component', () => {
        let comp: NotificatorDeleteDialogComponent;
        let fixture: ComponentFixture<NotificatorDeleteDialogComponent>;
        let service: NotificatorService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InvoisterTestModule],
                declarations: [NotificatorDeleteDialogComponent]
            })
                .overrideTemplate(NotificatorDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(NotificatorDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NotificatorService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
