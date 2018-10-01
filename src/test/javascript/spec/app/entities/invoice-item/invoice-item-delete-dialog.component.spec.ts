/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { InvoisterTestModule } from '../../../test.module';
import { InvoiceItemDeleteDialogComponent } from 'app/entities/invoice-item/invoice-item-delete-dialog.component';
import { InvoiceItemService } from 'app/entities/invoice-item/invoice-item.service';

describe('Component Tests', () => {
    describe('InvoiceItem Management Delete Component', () => {
        let comp: InvoiceItemDeleteDialogComponent;
        let fixture: ComponentFixture<InvoiceItemDeleteDialogComponent>;
        let service: InvoiceItemService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InvoisterTestModule],
                declarations: [InvoiceItemDeleteDialogComponent]
            })
                .overrideTemplate(InvoiceItemDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(InvoiceItemDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InvoiceItemService);
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
