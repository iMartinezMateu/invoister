/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { InvoisterTestModule } from '../../../test.module';
import { CostumerDeleteDialogComponent } from 'app/entities/costumer/costumer-delete-dialog.component';
import { CostumerService } from 'app/entities/costumer/costumer.service';

describe('Component Tests', () => {
    describe('Costumer Management Delete Component', () => {
        let comp: CostumerDeleteDialogComponent;
        let fixture: ComponentFixture<CostumerDeleteDialogComponent>;
        let service: CostumerService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InvoisterTestModule],
                declarations: [CostumerDeleteDialogComponent]
            })
                .overrideTemplate(CostumerDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CostumerDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CostumerService);
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
