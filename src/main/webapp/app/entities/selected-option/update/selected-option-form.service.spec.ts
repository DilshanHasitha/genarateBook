import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../selected-option.test-samples';

import { SelectedOptionFormService } from './selected-option-form.service';

describe('SelectedOption Form Service', () => {
  let service: SelectedOptionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SelectedOptionFormService);
  });

  describe('Service methods', () => {
    describe('createSelectedOptionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSelectedOptionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            date: expect.any(Object),
            books: expect.any(Object),
            customer: expect.any(Object),
            selectedOptionDetails: expect.any(Object),
          })
        );
      });

      it('passing ISelectedOption should create a new form with FormGroup', () => {
        const formGroup = service.createSelectedOptionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            date: expect.any(Object),
            books: expect.any(Object),
            customer: expect.any(Object),
            selectedOptionDetails: expect.any(Object),
          })
        );
      });
    });

    describe('getSelectedOption', () => {
      it('should return NewSelectedOption for default SelectedOption initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSelectedOptionFormGroup(sampleWithNewData);

        const selectedOption = service.getSelectedOption(formGroup) as any;

        expect(selectedOption).toMatchObject(sampleWithNewData);
      });

      it('should return NewSelectedOption for empty SelectedOption initial value', () => {
        const formGroup = service.createSelectedOptionFormGroup();

        const selectedOption = service.getSelectedOption(formGroup) as any;

        expect(selectedOption).toMatchObject({});
      });

      it('should return ISelectedOption', () => {
        const formGroup = service.createSelectedOptionFormGroup(sampleWithRequiredData);

        const selectedOption = service.getSelectedOption(formGroup) as any;

        expect(selectedOption).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISelectedOption should not enable id FormControl', () => {
        const formGroup = service.createSelectedOptionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSelectedOption should disable id FormControl', () => {
        const formGroup = service.createSelectedOptionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
