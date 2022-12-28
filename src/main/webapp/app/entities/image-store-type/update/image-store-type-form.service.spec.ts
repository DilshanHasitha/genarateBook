import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../image-store-type.test-samples';

import { ImageStoreTypeFormService } from './image-store-type-form.service';

describe('ImageStoreType Form Service', () => {
  let service: ImageStoreTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ImageStoreTypeFormService);
  });

  describe('Service methods', () => {
    describe('createImageStoreTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createImageStoreTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            imageStoreTypeCode: expect.any(Object),
            imageStoreTypeDescription: expect.any(Object),
            isActive: expect.any(Object),
          })
        );
      });

      it('passing IImageStoreType should create a new form with FormGroup', () => {
        const formGroup = service.createImageStoreTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            imageStoreTypeCode: expect.any(Object),
            imageStoreTypeDescription: expect.any(Object),
            isActive: expect.any(Object),
          })
        );
      });
    });

    describe('getImageStoreType', () => {
      it('should return NewImageStoreType for default ImageStoreType initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createImageStoreTypeFormGroup(sampleWithNewData);

        const imageStoreType = service.getImageStoreType(formGroup) as any;

        expect(imageStoreType).toMatchObject(sampleWithNewData);
      });

      it('should return NewImageStoreType for empty ImageStoreType initial value', () => {
        const formGroup = service.createImageStoreTypeFormGroup();

        const imageStoreType = service.getImageStoreType(formGroup) as any;

        expect(imageStoreType).toMatchObject({});
      });

      it('should return IImageStoreType', () => {
        const formGroup = service.createImageStoreTypeFormGroup(sampleWithRequiredData);

        const imageStoreType = service.getImageStoreType(formGroup) as any;

        expect(imageStoreType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IImageStoreType should not enable id FormControl', () => {
        const formGroup = service.createImageStoreTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewImageStoreType should disable id FormControl', () => {
        const formGroup = service.createImageStoreTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
