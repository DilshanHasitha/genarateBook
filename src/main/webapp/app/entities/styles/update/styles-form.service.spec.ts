import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../styles.test-samples';

import { StylesFormService } from './styles-form.service';

describe('Styles Form Service', () => {
  let service: StylesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StylesFormService);
  });

  describe('Service methods', () => {
    describe('createStylesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createStylesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            description: expect.any(Object),
            imgURL: expect.any(Object),
            isActive: expect.any(Object),
            width: expect.any(Object),
            height: expect.any(Object),
            x: expect.any(Object),
            y: expect.any(Object),
          })
        );
      });

      it('passing IStyles should create a new form with FormGroup', () => {
        const formGroup = service.createStylesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            description: expect.any(Object),
            imgURL: expect.any(Object),
            isActive: expect.any(Object),
            width: expect.any(Object),
            height: expect.any(Object),
            x: expect.any(Object),
            y: expect.any(Object),
          })
        );
      });
    });

    describe('getStyles', () => {
      it('should return NewStyles for default Styles initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createStylesFormGroup(sampleWithNewData);

        const styles = service.getStyles(formGroup) as any;

        expect(styles).toMatchObject(sampleWithNewData);
      });

      it('should return NewStyles for empty Styles initial value', () => {
        const formGroup = service.createStylesFormGroup();

        const styles = service.getStyles(formGroup) as any;

        expect(styles).toMatchObject({});
      });

      it('should return IStyles', () => {
        const formGroup = service.createStylesFormGroup(sampleWithRequiredData);

        const styles = service.getStyles(formGroup) as any;

        expect(styles).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IStyles should not enable id FormControl', () => {
        const formGroup = service.createStylesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewStyles should disable id FormControl', () => {
        const formGroup = service.createStylesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
