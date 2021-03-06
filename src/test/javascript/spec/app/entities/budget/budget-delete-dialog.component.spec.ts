/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { InvoisterTestModule } from '../../../test.module';
import { BudgetDeleteDialogComponent } from 'app/entities/budget/budget-delete-dialog.component';
import { BudgetService } from 'app/entities/budget/budget.service';

describe('Component Tests', () => {
    describe('Budget Management Delete Component', () => {
        let comp: BudgetDeleteDialogComponent;
        let fixture: ComponentFixture<BudgetDeleteDialogComponent>;
        let service: BudgetService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InvoisterTestModule],
                declarations: [BudgetDeleteDialogComponent]
            })
                .overrideTemplate(BudgetDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BudgetDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BudgetService);
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
