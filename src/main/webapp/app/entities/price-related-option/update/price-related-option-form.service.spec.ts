import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../price-related-option.test-samples';

import { PriceRelatedOptionFormService } from './price-related-option-form.service';

describe('PriceRelatedOption Form Service', () => {
  let service: PriceRelatedOptionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PriceRelatedOptionFormService);
  });

  describe('Service methods', () => {
    describe('createPriceRelatedOptionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPriceRelatedOptionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            isActive: expect.any(Object),
            optionType: expect.any(Object),
            priceRelatedOptionDetails: expect.any(Object),
            books: expect.any(Object),
          })
        );
      });

      it('passing IPriceRelatedOption should create a new form with FormGroup', () => {
        const formGroup = service.createPriceRelatedOptionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            isActive: expect.any(Object),
            optionType: expect.any(Object),
            priceRelatedOptionDetails: expect.any(Object),
            books: expect.any(Object),
          })
        );
      });
    });

    describe('getPriceRelatedOption', () => {
      it('should return NewPriceRelatedOption for default PriceRelatedOption initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPriceRelatedOptionFormGroup(sampleWithNewData);

        const priceRelatedOption = service.getPriceRelatedOption(formGroup) as any;

        expect(priceRelatedOption).toMatchObject(sampleWithNewData);
      });

      it('should return NewPriceRelatedOption for empty PriceRelatedOption initial value', () => {
        const formGroup = service.createPriceRelatedOptionFormGroup();

        const priceRelatedOption = service.getPriceRelatedOption(formGroup) as any;

        expect(priceRelatedOption).toMatchObject({});
      });

      it('should return IPriceRelatedOption', () => {
        const formGroup = service.createPriceRelatedOptionFormGroup(sampleWithRequiredData);

        const priceRelatedOption = service.getPriceRelatedOption(formGroup) as any;

        expect(priceRelatedOption).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPriceRelatedOption should not enable id FormControl', () => {
        const formGroup = service.createPriceRelatedOptionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPriceRelatedOption should disable id FormControl', () => {
        const formGroup = service.createPriceRelatedOptionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
