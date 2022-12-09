import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../avatar-attributes.test-samples';

import { AvatarAttributesFormService } from './avatar-attributes-form.service';

describe('AvatarAttributes Form Service', () => {
  let service: AvatarAttributesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AvatarAttributesFormService);
  });

  describe('Service methods', () => {
    describe('createAvatarAttributesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAvatarAttributesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            description: expect.any(Object),
            isActive: expect.any(Object),
            avatarAttributesCode: expect.any(Object),
            avatarCharactors: expect.any(Object),
            books: expect.any(Object),
            styles: expect.any(Object),
            options: expect.any(Object),
          })
        );
      });

      it('passing IAvatarAttributes should create a new form with FormGroup', () => {
        const formGroup = service.createAvatarAttributesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            description: expect.any(Object),
            isActive: expect.any(Object),
            avatarAttributesCode: expect.any(Object),
            avatarCharactors: expect.any(Object),
            books: expect.any(Object),
            styles: expect.any(Object),
            options: expect.any(Object),
          })
        );
      });
    });

    describe('getAvatarAttributes', () => {
      it('should return NewAvatarAttributes for default AvatarAttributes initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createAvatarAttributesFormGroup(sampleWithNewData);

        const avatarAttributes = service.getAvatarAttributes(formGroup) as any;

        expect(avatarAttributes).toMatchObject(sampleWithNewData);
      });

      it('should return NewAvatarAttributes for empty AvatarAttributes initial value', () => {
        const formGroup = service.createAvatarAttributesFormGroup();

        const avatarAttributes = service.getAvatarAttributes(formGroup) as any;

        expect(avatarAttributes).toMatchObject({});
      });

      it('should return IAvatarAttributes', () => {
        const formGroup = service.createAvatarAttributesFormGroup(sampleWithRequiredData);

        const avatarAttributes = service.getAvatarAttributes(formGroup) as any;

        expect(avatarAttributes).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAvatarAttributes should not enable id FormControl', () => {
        const formGroup = service.createAvatarAttributesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAvatarAttributes should disable id FormControl', () => {
        const formGroup = service.createAvatarAttributesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
