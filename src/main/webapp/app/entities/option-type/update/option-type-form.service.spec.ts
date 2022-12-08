import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../option-type.test-samples';

import { OptionTypeFormService } from './option-type-form.service';

describe('OptionType Form Service', () => {
  let service: OptionTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OptionTypeFormService);
  });

  describe('Service methods', () => {
    describe('createOptionTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOptionTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            description: expect.any(Object),
            isActive: expect.any(Object),
          })
        );
      });

      it('passing IOptionType should create a new form with FormGroup', () => {
        const formGroup = service.createOptionTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            description: expect.any(Object),
            isActive: expect.any(Object),
          })
        );
      });
    });

    describe('getOptionType', () => {
      it('should return NewOptionType for default OptionType initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createOptionTypeFormGroup(sampleWithNewData);

        const optionType = service.getOptionType(formGroup) as any;

        expect(optionType).toMatchObject(sampleWithNewData);
      });

      it('should return NewOptionType for empty OptionType initial value', () => {
        const formGroup = service.createOptionTypeFormGroup();

        const optionType = service.getOptionType(formGroup) as any;

        expect(optionType).toMatchObject({});
      });

      it('should return IOptionType', () => {
        const formGroup = service.createOptionTypeFormGroup(sampleWithRequiredData);

        const optionType = service.getOptionType(formGroup) as any;

        expect(optionType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOptionType should not enable id FormControl', () => {
        const formGroup = service.createOptionTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOptionType should disable id FormControl', () => {
        const formGroup = service.createOptionTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
