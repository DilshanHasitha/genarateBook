import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../options.test-samples';

import { OptionsFormService } from './options-form.service';

describe('Options Form Service', () => {
  let service: OptionsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OptionsFormService);
  });

  describe('Service methods', () => {
    describe('createOptionsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOptionsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            description: expect.any(Object),
            imgURL: expect.any(Object),
            isActive: expect.any(Object),
            styles: expect.any(Object),
            avatarAttributes: expect.any(Object),
          })
        );
      });

      it('passing IOptions should create a new form with FormGroup', () => {
        const formGroup = service.createOptionsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            description: expect.any(Object),
            imgURL: expect.any(Object),
            isActive: expect.any(Object),
            styles: expect.any(Object),
            avatarAttributes: expect.any(Object),
          })
        );
      });
    });

    describe('getOptions', () => {
      it('should return NewOptions for default Options initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createOptionsFormGroup(sampleWithNewData);

        const options = service.getOptions(formGroup) as any;

        expect(options).toMatchObject(sampleWithNewData);
      });

      it('should return NewOptions for empty Options initial value', () => {
        const formGroup = service.createOptionsFormGroup();

        const options = service.getOptions(formGroup) as any;

        expect(options).toMatchObject({});
      });

      it('should return IOptions', () => {
        const formGroup = service.createOptionsFormGroup(sampleWithRequiredData);

        const options = service.getOptions(formGroup) as any;

        expect(options).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOptions should not enable id FormControl', () => {
        const formGroup = service.createOptionsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOptions should disable id FormControl', () => {
        const formGroup = service.createOptionsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
