/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { InvoisterTestModule } from '../../../test.module';
import { InvoiceItemComponent } from 'app/entities/invoice-item/invoice-item.component';
import { InvoiceItemService } from 'app/entities/invoice-item/invoice-item.service';
import { InvoiceItem } from 'app/shared/model/invoice-item.model';

describe('Component Tests', () => {
    describe('InvoiceItem Management Component', () => {
        let comp: InvoiceItemComponent;
        let fixture: ComponentFixture<InvoiceItemComponent>;
        let service: InvoiceItemService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InvoisterTestModule],
                declarations: [InvoiceItemComponent],
                providers: []
            })
                .overrideTemplate(InvoiceItemComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(InvoiceItemComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InvoiceItemService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new InvoiceItem(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.invoiceItems[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
