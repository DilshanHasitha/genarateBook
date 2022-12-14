import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../avatar-charactor.test-samples';

import { AvatarCharactorFormService } from './avatar-charactor-form.service';

describe('AvatarCharactor Form Service', () => {
  let service: AvatarCharactorFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AvatarCharactorFormService);
  });

  describe('Service methods', () => {
    describe('createAvatarCharactorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAvatarCharactorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            description: expect.any(Object),
            isActive: expect.any(Object),
            imgUrl: expect.any(Object),
            width: expect.any(Object),
            height: expect.any(Object),
            x: expect.any(Object),
            y: expect.any(Object),
            avatarAttributes: expect.any(Object),
            layerGroup: expect.any(Object),
            character: expect.any(Object),
          })
        );
      });

      it('passing IAvatarCharactor should create a new form with FormGroup', () => {
        const formGroup = service.createAvatarCharactorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            description: expect.any(Object),
            isActive: expect.any(Object),
            imgUrl: expect.any(Object),
            width: expect.any(Object),
            height: expect.any(Object),
            x: expect.any(Object),
            y: expect.any(Object),
            avatarAttributes: expect.any(Object),
            layerGroup: expect.any(Object),
            character: expect.any(Object),
          })
        );
      });
    });

    describe('getAvatarCharactor', () => {
      it('should return NewAvatarCharactor for default AvatarCharactor initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createAvatarCharactorFormGroup(sampleWithNewData);

        const avatarCharactor = service.getAvatarCharactor(formGroup) as any;

        expect(avatarCharactor).toMatchObject(sampleWithNewData);
      });

      it('should return NewAvatarCharactor for empty AvatarCharactor initial value', () => {
        const formGroup = service.createAvatarCharactorFormGroup();

        const avatarCharactor = service.getAvatarCharactor(formGroup) as any;

        expect(avatarCharactor).toMatchObject({});
      });

      it('should return IAvatarCharactor', () => {
        const formGroup = service.createAvatarCharactorFormGroup(sampleWithRequiredData);

        const avatarCharactor = service.getAvatarCharactor(formGroup) as any;

        expect(avatarCharactor).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAvatarCharactor should not enable id FormControl', () => {
        const formGroup = service.createAvatarCharactorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAvatarCharactor should disable id FormControl', () => {
        const formGroup = service.createAvatarCharactorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
