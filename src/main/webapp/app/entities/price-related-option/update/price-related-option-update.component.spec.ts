import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PriceRelatedOptionFormService } from './price-related-option-form.service';
import { PriceRelatedOptionService } from '../service/price-related-option.service';
import { IPriceRelatedOption } from '../price-related-option.model';
import { IOptionType } from 'app/entities/option-type/option-type.model';
import { OptionTypeService } from 'app/entities/option-type/service/option-type.service';
import { IPriceRelatedOptionDetails } from 'app/entities/price-related-option-details/price-related-option-details.model';
import { PriceRelatedOptionDetailsService } from 'app/entities/price-related-option-details/service/price-related-option-details.service';

import { PriceRelatedOptionUpdateComponent } from './price-related-option-update.component';

describe('PriceRelatedOption Management Update Component', () => {
  let comp: PriceRelatedOptionUpdateComponent;
  let fixture: ComponentFixture<PriceRelatedOptionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let priceRelatedOptionFormService: PriceRelatedOptionFormService;
  let priceRelatedOptionService: PriceRelatedOptionService;
  let optionTypeService: OptionTypeService;
  let priceRelatedOptionDetailsService: PriceRelatedOptionDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PriceRelatedOptionUpdateComponent],
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
      .overrideTemplate(PriceRelatedOptionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PriceRelatedOptionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    priceRelatedOptionFormService = TestBed.inject(PriceRelatedOptionFormService);
    priceRelatedOptionService = TestBed.inject(PriceRelatedOptionService);
    optionTypeService = TestBed.inject(OptionTypeService);
    priceRelatedOptionDetailsService = TestBed.inject(PriceRelatedOptionDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call OptionType query and add missing value', () => {
      const priceRelatedOption: IPriceRelatedOption = { id: 456 };
      const optionType: IOptionType = { id: 85279 };
      priceRelatedOption.optionType = optionType;

      const optionTypeCollection: IOptionType[] = [{ id: 29620 }];
      jest.spyOn(optionTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: optionTypeCollection })));
      const additionalOptionTypes = [optionType];
      const expectedCollection: IOptionType[] = [...additionalOptionTypes, ...optionTypeCollection];
      jest.spyOn(optionTypeService, 'addOptionTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ priceRelatedOption });
      comp.ngOnInit();

      expect(optionTypeService.query).toHaveBeenCalled();
      expect(optionTypeService.addOptionTypeToCollectionIfMissing).toHaveBeenCalledWith(
        optionTypeCollection,
        ...additionalOptionTypes.map(expect.objectContaining)
      );
      expect(comp.optionTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PriceRelatedOptionDetails query and add missing value', () => {
      const priceRelatedOption: IPriceRelatedOption = { id: 456 };
      const priceRelatedOptionDetails: IPriceRelatedOptionDetails[] = [{ id: 92669 }];
      priceRelatedOption.priceRelatedOptionDetails = priceRelatedOptionDetails;

      const priceRelatedOptionDetailsCollection: IPriceRelatedOptionDetails[] = [{ id: 82427 }];
      jest
        .spyOn(priceRelatedOptionDetailsService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: priceRelatedOptionDetailsCollection })));
      const additionalPriceRelatedOptionDetails = [...priceRelatedOptionDetails];
      const expectedCollection: IPriceRelatedOptionDetails[] = [
        ...additionalPriceRelatedOptionDetails,
        ...priceRelatedOptionDetailsCollection,
      ];
      jest.spyOn(priceRelatedOptionDetailsService, 'addPriceRelatedOptionDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ priceRelatedOption });
      comp.ngOnInit();

      expect(priceRelatedOptionDetailsService.query).toHaveBeenCalled();
      expect(priceRelatedOptionDetailsService.addPriceRelatedOptionDetailsToCollectionIfMissing).toHaveBeenCalledWith(
        priceRelatedOptionDetailsCollection,
        ...additionalPriceRelatedOptionDetails.map(expect.objectContaining)
      );
      expect(comp.priceRelatedOptionDetailsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const priceRelatedOption: IPriceRelatedOption = { id: 456 };
      const optionType: IOptionType = { id: 6398 };
      priceRelatedOption.optionType = optionType;
      const priceRelatedOptionDetails: IPriceRelatedOptionDetails = { id: 37662 };
      priceRelatedOption.priceRelatedOptionDetails = [priceRelatedOptionDetails];

      activatedRoute.data = of({ priceRelatedOption });
      comp.ngOnInit();

      expect(comp.optionTypesSharedCollection).toContain(optionType);
      expect(comp.priceRelatedOptionDetailsSharedCollection).toContain(priceRelatedOptionDetails);
      expect(comp.priceRelatedOption).toEqual(priceRelatedOption);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPriceRelatedOption>>();
      const priceRelatedOption = { id: 123 };
      jest.spyOn(priceRelatedOptionFormService, 'getPriceRelatedOption').mockReturnValue(priceRelatedOption);
      jest.spyOn(priceRelatedOptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ priceRelatedOption });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: priceRelatedOption }));
      saveSubject.complete();

      // THEN
      expect(priceRelatedOptionFormService.getPriceRelatedOption).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(priceRelatedOptionService.update).toHaveBeenCalledWith(expect.objectContaining(priceRelatedOption));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPriceRelatedOption>>();
      const priceRelatedOption = { id: 123 };
      jest.spyOn(priceRelatedOptionFormService, 'getPriceRelatedOption').mockReturnValue({ id: null });
      jest.spyOn(priceRelatedOptionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ priceRelatedOption: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: priceRelatedOption }));
      saveSubject.complete();

      // THEN
      expect(priceRelatedOptionFormService.getPriceRelatedOption).toHaveBeenCalled();
      expect(priceRelatedOptionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPriceRelatedOption>>();
      const priceRelatedOption = { id: 123 };
      jest.spyOn(priceRelatedOptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ priceRelatedOption });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(priceRelatedOptionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareOptionType', () => {
      it('Should forward to optionTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(optionTypeService, 'compareOptionType');
        comp.compareOptionType(entity, entity2);
        expect(optionTypeService.compareOptionType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePriceRelatedOptionDetails', () => {
      it('Should forward to priceRelatedOptionDetailsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(priceRelatedOptionDetailsService, 'comparePriceRelatedOptionDetails');
        comp.comparePriceRelatedOptionDetails(entity, entity2);
        expect(priceRelatedOptionDetailsService.comparePriceRelatedOptionDetails).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
