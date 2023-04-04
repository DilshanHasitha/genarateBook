import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../org-property.test-samples';

import { OrgPropertyFormService } from './org-property-form.service';

describe('OrgProperty Form Service', () => {
  let service: OrgPropertyFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrgPropertyFormService);
  });

  describe('Service methods', () => {
    describe('createOrgPropertyFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrgPropertyFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            isActive: expect.any(Object),
          })
        );
      });

      it('passing IOrgProperty should create a new form with FormGroup', () => {
        const formGroup = service.createOrgPropertyFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            isActive: expect.any(Object),
          })
        );
      });
    });

    describe('getOrgProperty', () => {
      it('should return NewOrgProperty for default OrgProperty initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createOrgPropertyFormGroup(sampleWithNewData);

        const orgProperty = service.getOrgProperty(formGroup) as any;

        expect(orgProperty).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrgProperty for empty OrgProperty initial value', () => {
        const formGroup = service.createOrgPropertyFormGroup();

        const orgProperty = service.getOrgProperty(formGroup) as any;

        expect(orgProperty).toMatchObject({});
      });

      it('should return IOrgProperty', () => {
        const formGroup = service.createOrgPropertyFormGroup(sampleWithRequiredData);

        const orgProperty = service.getOrgProperty(formGroup) as any;

        expect(orgProperty).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrgProperty should not enable id FormControl', () => {
        const formGroup = service.createOrgPropertyFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrgProperty should disable id FormControl', () => {
        const formGroup = service.createOrgPropertyFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
