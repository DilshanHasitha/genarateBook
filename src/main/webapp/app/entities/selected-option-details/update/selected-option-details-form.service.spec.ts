import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../selected-option-details.test-samples';

import { SelectedOptionDetailsFormService } from './selected-option-details-form.service';

describe('SelectedOptionDetails Form Service', () => {
  let service: SelectedOptionDetailsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SelectedOptionDetailsFormService);
  });

  describe('Service methods', () => {
    describe('createSelectedOptionDetailsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSelectedOptionDetailsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            selectedValue: expect.any(Object),
            isActive: expect.any(Object),
            selectedStyleCode: expect.any(Object),
            selectedOptionCode: expect.any(Object),
            selectedOptions: expect.any(Object),
          })
        );
      });

      it('passing ISelectedOptionDetails should create a new form with FormGroup', () => {
        const formGroup = service.createSelectedOptionDetailsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            selectedValue: expect.any(Object),
            isActive: expect.any(Object),
            selectedStyleCode: expect.any(Object),
            selectedOptionCode: expect.any(Object),
            selectedOptions: expect.any(Object),
          })
        );
      });
    });

    describe('getSelectedOptionDetails', () => {
      it('should return NewSelectedOptionDetails for default SelectedOptionDetails initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSelectedOptionDetailsFormGroup(sampleWithNewData);

        const selectedOptionDetails = service.getSelectedOptionDetails(formGroup) as any;

        expect(selectedOptionDetails).toMatchObject(sampleWithNewData);
      });

      it('should return NewSelectedOptionDetails for empty SelectedOptionDetails initial value', () => {
        const formGroup = service.createSelectedOptionDetailsFormGroup();

        const selectedOptionDetails = service.getSelectedOptionDetails(formGroup) as any;

        expect(selectedOptionDetails).toMatchObject({});
      });

      it('should return ISelectedOptionDetails', () => {
        const formGroup = service.createSelectedOptionDetailsFormGroup(sampleWithRequiredData);

        const selectedOptionDetails = service.getSelectedOptionDetails(formGroup) as any;

        expect(selectedOptionDetails).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISelectedOptionDetails should not enable id FormControl', () => {
        const formGroup = service.createSelectedOptionDetailsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSelectedOptionDetails should disable id FormControl', () => {
        const formGroup = service.createSelectedOptionDetailsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
