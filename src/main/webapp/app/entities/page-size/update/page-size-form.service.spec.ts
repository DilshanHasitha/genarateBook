import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../page-size.test-samples';

import { PageSizeFormService } from './page-size-form.service';

describe('PageSize Form Service', () => {
  let service: PageSizeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PageSizeFormService);
  });

  describe('Service methods', () => {
    describe('createPageSizeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPageSizeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            description: expect.any(Object),
            isActive: expect.any(Object),
            width: expect.any(Object),
            height: expect.any(Object),
          })
        );
      });

      it('passing IPageSize should create a new form with FormGroup', () => {
        const formGroup = service.createPageSizeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            description: expect.any(Object),
            isActive: expect.any(Object),
            width: expect.any(Object),
            height: expect.any(Object),
          })
        );
      });
    });

    describe('getPageSize', () => {
      it('should return NewPageSize for default PageSize initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPageSizeFormGroup(sampleWithNewData);

        const pageSize = service.getPageSize(formGroup) as any;

        expect(pageSize).toMatchObject(sampleWithNewData);
      });

      it('should return NewPageSize for empty PageSize initial value', () => {
        const formGroup = service.createPageSizeFormGroup();

        const pageSize = service.getPageSize(formGroup) as any;

        expect(pageSize).toMatchObject({});
      });

      it('should return IPageSize', () => {
        const formGroup = service.createPageSizeFormGroup(sampleWithRequiredData);

        const pageSize = service.getPageSize(formGroup) as any;

        expect(pageSize).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPageSize should not enable id FormControl', () => {
        const formGroup = service.createPageSizeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPageSize should disable id FormControl', () => {
        const formGroup = service.createPageSizeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
