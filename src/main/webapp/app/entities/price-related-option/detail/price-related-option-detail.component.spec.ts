import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PriceRelatedOptionDetailComponent } from './price-related-option-detail.component';

describe('PriceRelatedOption Management Detail Component', () => {
  let comp: PriceRelatedOptionDetailComponent;
  let fixture: ComponentFixture<PriceRelatedOptionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PriceRelatedOptionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ priceRelatedOption: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PriceRelatedOptionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PriceRelatedOptionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load priceRelatedOption on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.priceRelatedOption).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
