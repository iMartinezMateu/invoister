/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { InvoisterTestModule } from '../../../test.module';
import { CostumerUpdateComponent } from 'app/entities/costumer/costumer-update.component';
import { CostumerService } from 'app/entities/costumer/costumer.service';
import { Costumer } from 'app/shared/model/costumer.model';

describe('Component Tests', () => {
    describe('Costumer Management Update Component', () => {
        let comp: CostumerUpdateComponent;
        let fixture: ComponentFixture<CostumerUpdateComponent>;
        let service: CostumerService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InvoisterTestModule],
                declarations: [CostumerUpdateComponent]
            })
                .overrideTemplate(CostumerUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CostumerUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CostumerService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Costumer(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.costumer = entity;
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
                    const entity = new Costumer();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.costumer = entity;
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
