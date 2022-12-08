import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PriceRelatedOptionDetailsFormService } from './price-related-option-details-form.service';
import { PriceRelatedOptionDetailsService } from '../service/price-related-option-details.service';
import { IPriceRelatedOptionDetails } from '../price-related-option-details.model';

import { PriceRelatedOptionDetailsUpdateComponent } from './price-related-option-details-update.component';

describe('PriceRelatedOptionDetails Management Update Component', () => {
  let comp: PriceRelatedOptionDetailsUpdateComponent;
  let fixture: ComponentFixture<PriceRelatedOptionDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let priceRelatedOptionDetailsFormService: PriceRelatedOptionDetailsFormService;
  let priceRelatedOptionDetailsService: PriceRelatedOptionDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PriceRelatedOptionDetailsUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PriceRelatedOptionDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PriceRelatedOptionDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    priceRelatedOptionDetailsFormService = TestBed.inject(PriceRelatedOptionDetailsFormService);
    priceRelatedOptionDetailsService = TestBed.inject(PriceRelatedOptionDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const priceRelatedOptionDetails: IPriceRelatedOptionDetails = { id: 456 };

      activatedRoute.data = of({ priceRelatedOptionDetails });
      comp.ngOnInit();

      expect(comp.priceRelatedOptionDetails).toEqual(priceRelatedOptionDetails);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPriceRelatedOptionDetails>>();
      const priceRelatedOptionDetails = { id: 123 };
      jest.spyOn(priceRelatedOptionDetailsFormService, 'getPriceRelatedOptionDetails').mockReturnValue(priceRelatedOptionDetails);
      jest.spyOn(priceRelatedOptionDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ priceRelatedOptionDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: priceRelatedOptionDetails }));
      saveSubject.complete();

      // THEN
      expect(priceRelatedOptionDetailsFormService.getPriceRelatedOptionDetails).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(priceRelatedOptionDetailsService.update).toHaveBeenCalledWith(expect.objectContaining(priceRelatedOptionDetails));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPriceRelatedOptionDetails>>();
      const priceRelatedOptionDetails = { id: 123 };
      jest.spyOn(priceRelatedOptionDetailsFormService, 'getPriceRelatedOptionDetails').mockReturnValue({ id: null });
      jest.spyOn(priceRelatedOptionDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ priceRelatedOptionDetails: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: priceRelatedOptionDetails }));
      saveSubject.complete();

      // THEN
      expect(priceRelatedOptionDetailsFormService.getPriceRelatedOptionDetails).toHaveBeenCalled();
      expect(priceRelatedOptionDetailsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPriceRelatedOptionDetails>>();
      const priceRelatedOptionDetails = { id: 123 };
      jest.spyOn(priceRelatedOptionDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ priceRelatedOptionDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(priceRelatedOptionDetailsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
