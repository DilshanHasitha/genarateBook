import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../styles-details.test-samples';

import { StylesDetailsFormService } from './styles-details-form.service';

describe('StylesDetails Form Service', () => {
  let service: StylesDetailsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StylesDetailsFormService);
  });

  describe('Service methods', () => {
    describe('createStylesDetailsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createStylesDetailsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            isActive: expect.any(Object),
            templateValue: expect.any(Object),
            replaceValue: expect.any(Object),
          })
        );
      });

      it('passing IStylesDetails should create a new form with FormGroup', () => {
        const formGroup = service.createStylesDetailsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            isActive: expect.any(Object),
            templateValue: expect.any(Object),
            replaceValue: expect.any(Object),
          })
        );
      });
    });

    describe('getStylesDetails', () => {
      it('should return NewStylesDetails for default StylesDetails initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createStylesDetailsFormGroup(sampleWithNewData);

        const stylesDetails = service.getStylesDetails(formGroup) as any;

        expect(stylesDetails).toMatchObject(sampleWithNewData);
      });

      it('should return NewStylesDetails for empty StylesDetails initial value', () => {
        const formGroup = service.createStylesDetailsFormGroup();

        const stylesDetails = service.getStylesDetails(formGroup) as any;

        expect(stylesDetails).toMatchObject({});
      });

      it('should return IStylesDetails', () => {
        const formGroup = service.createStylesDetailsFormGroup(sampleWithRequiredData);

        const stylesDetails = service.getStylesDetails(formGroup) as any;

        expect(stylesDetails).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IStylesDetails should not enable id FormControl', () => {
        const formGroup = service.createStylesDetailsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewStylesDetails should disable id FormControl', () => {
        const formGroup = service.createStylesDetailsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
