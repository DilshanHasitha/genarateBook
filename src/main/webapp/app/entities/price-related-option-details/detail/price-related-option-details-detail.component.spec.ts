import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PriceRelatedOptionDetailsDetailComponent } from './price-related-option-details-detail.component';

describe('PriceRelatedOptionDetails Management Detail Component', () => {
  let comp: PriceRelatedOptionDetailsDetailComponent;
  let fixture: ComponentFixture<PriceRelatedOptionDetailsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PriceRelatedOptionDetailsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ priceRelatedOptionDetails: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PriceRelatedOptionDetailsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PriceRelatedOptionDetailsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load priceRelatedOptionDetails on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.priceRelatedOptionDetails).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
