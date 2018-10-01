/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InvoisterTestModule } from '../../../test.module';
import { CostumerDetailComponent } from 'app/entities/costumer/costumer-detail.component';
import { Costumer } from 'app/shared/model/costumer.model';

describe('Component Tests', () => {
    describe('Costumer Management Detail Component', () => {
        let comp: CostumerDetailComponent;
        let fixture: ComponentFixture<CostumerDetailComponent>;
        const route = ({ data: of({ costumer: new Costumer(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InvoisterTestModule],
                declarations: [CostumerDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CostumerDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CostumerDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.costumer).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
