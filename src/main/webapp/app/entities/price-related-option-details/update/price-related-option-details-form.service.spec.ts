import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../price-related-option-details.test-samples';

import { PriceRelatedOptionDetailsFormService } from './price-related-option-details-form.service';

describe('PriceRelatedOptionDetails Form Service', () => {
  let service: PriceRelatedOptionDetailsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PriceRelatedOptionDetailsFormService);
  });

  describe('Service methods', () => {
    describe('createPriceRelatedOptionDetailsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPriceRelatedOptionDetailsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            price: expect.any(Object),
            priceRelatedOptions: expect.any(Object),
          })
        );
      });

      it('passing IPriceRelatedOptionDetails should create a new form with FormGroup', () => {
        const formGroup = service.createPriceRelatedOptionDetailsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            price: expect.any(Object),
            priceRelatedOptions: expect.any(Object),
          })
        );
      });
    });

    describe('getPriceRelatedOptionDetails', () => {
      it('should return NewPriceRelatedOptionDetails for default PriceRelatedOptionDetails initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPriceRelatedOptionDetailsFormGroup(sampleWithNewData);

        const priceRelatedOptionDetails = service.getPriceRelatedOptionDetails(formGroup) as any;

        expect(priceRelatedOptionDetails).toMatchObject(sampleWithNewData);
      });

      it('should return NewPriceRelatedOptionDetails for empty PriceRelatedOptionDetails initial value', () => {
        const formGroup = service.createPriceRelatedOptionDetailsFormGroup();

        const priceRelatedOptionDetails = service.getPriceRelatedOptionDetails(formGroup) as any;

        expect(priceRelatedOptionDetails).toMatchObject({});
      });

      it('should return IPriceRelatedOptionDetails', () => {
        const formGroup = service.createPriceRelatedOptionDetailsFormGroup(sampleWithRequiredData);

        const priceRelatedOptionDetails = service.getPriceRelatedOptionDetails(formGroup) as any;

        expect(priceRelatedOptionDetails).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPriceRelatedOptionDetails should not enable id FormControl', () => {
        const formGroup = service.createPriceRelatedOptionDetailsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPriceRelatedOptionDetails should disable id FormControl', () => {
        const formGroup = service.createPriceRelatedOptionDetailsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
