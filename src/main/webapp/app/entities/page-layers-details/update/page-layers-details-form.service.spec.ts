import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../page-layers-details.test-samples';

import { PageLayersDetailsFormService } from './page-layers-details-form.service';

describe('PageLayersDetails Form Service', () => {
  let service: PageLayersDetailsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PageLayersDetailsFormService);
  });

  describe('Service methods', () => {
    describe('createPageLayersDetailsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPageLayersDetailsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            isActive: expect.any(Object),
            pageElements: expect.any(Object),
          })
        );
      });

      it('passing IPageLayersDetails should create a new form with FormGroup', () => {
        const formGroup = service.createPageLayersDetailsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            isActive: expect.any(Object),
            pageElements: expect.any(Object),
          })
        );
      });
    });

    describe('getPageLayersDetails', () => {
      it('should return NewPageLayersDetails for default PageLayersDetails initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPageLayersDetailsFormGroup(sampleWithNewData);

        const pageLayersDetails = service.getPageLayersDetails(formGroup) as any;

        expect(pageLayersDetails).toMatchObject(sampleWithNewData);
      });

      it('should return NewPageLayersDetails for empty PageLayersDetails initial value', () => {
        const formGroup = service.createPageLayersDetailsFormGroup();

        const pageLayersDetails = service.getPageLayersDetails(formGroup) as any;

        expect(pageLayersDetails).toMatchObject({});
      });

      it('should return IPageLayersDetails', () => {
        const formGroup = service.createPageLayersDetailsFormGroup(sampleWithRequiredData);

        const pageLayersDetails = service.getPageLayersDetails(formGroup) as any;

        expect(pageLayersDetails).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPageLayersDetails should not enable id FormControl', () => {
        const formGroup = service.createPageLayersDetailsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPageLayersDetails should disable id FormControl', () => {
        const formGroup = service.createPageLayersDetailsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
