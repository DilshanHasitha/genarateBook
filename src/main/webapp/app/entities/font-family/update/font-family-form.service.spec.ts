import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../font-family.test-samples';

import { FontFamilyFormService } from './font-family-form.service';

describe('FontFamily Form Service', () => {
  let service: FontFamilyFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FontFamilyFormService);
  });

  describe('Service methods', () => {
    describe('createFontFamilyFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFontFamilyFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            url: expect.any(Object),
            isActive: expect.any(Object),
          })
        );
      });

      it('passing IFontFamily should create a new form with FormGroup', () => {
        const formGroup = service.createFontFamilyFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            url: expect.any(Object),
            isActive: expect.any(Object),
          })
        );
      });
    });

    describe('getFontFamily', () => {
      it('should return NewFontFamily for default FontFamily initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFontFamilyFormGroup(sampleWithNewData);

        const fontFamily = service.getFontFamily(formGroup) as any;

        expect(fontFamily).toMatchObject(sampleWithNewData);
      });

      it('should return NewFontFamily for empty FontFamily initial value', () => {
        const formGroup = service.createFontFamilyFormGroup();

        const fontFamily = service.getFontFamily(formGroup) as any;

        expect(fontFamily).toMatchObject({});
      });

      it('should return IFontFamily', () => {
        const formGroup = service.createFontFamilyFormGroup(sampleWithRequiredData);

        const fontFamily = service.getFontFamily(formGroup) as any;

        expect(fontFamily).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFontFamily should not enable id FormControl', () => {
        const formGroup = service.createFontFamilyFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFontFamily should disable id FormControl', () => {
        const formGroup = service.createFontFamilyFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
