import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../page-layers.test-samples';

import { PageLayersFormService } from './page-layers-form.service';

describe('PageLayers Form Service', () => {
  let service: PageLayersFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PageLayersFormService);
  });

  describe('Service methods', () => {
    describe('createPageLayersFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPageLayersFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            layerNo: expect.any(Object),
            isActive: expect.any(Object),
            isEditable: expect.any(Object),
            isText: expect.any(Object),
            pageElementDetails: expect.any(Object),
            booksPages: expect.any(Object),
          })
        );
      });

      it('passing IPageLayers should create a new form with FormGroup', () => {
        const formGroup = service.createPageLayersFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            layerNo: expect.any(Object),
            isActive: expect.any(Object),
            isEditable: expect.any(Object),
            isText: expect.any(Object),
            pageElementDetails: expect.any(Object),
            booksPages: expect.any(Object),
          })
        );
      });
    });

    describe('getPageLayers', () => {
      it('should return NewPageLayers for default PageLayers initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPageLayersFormGroup(sampleWithNewData);

        const pageLayers = service.getPageLayers(formGroup) as any;

        expect(pageLayers).toMatchObject(sampleWithNewData);
      });

      it('should return NewPageLayers for empty PageLayers initial value', () => {
        const formGroup = service.createPageLayersFormGroup();

        const pageLayers = service.getPageLayers(formGroup) as any;

        expect(pageLayers).toMatchObject({});
      });

      it('should return IPageLayers', () => {
        const formGroup = service.createPageLayersFormGroup(sampleWithRequiredData);

        const pageLayers = service.getPageLayers(formGroup) as any;

        expect(pageLayers).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPageLayers should not enable id FormControl', () => {
        const formGroup = service.createPageLayersFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPageLayers should disable id FormControl', () => {
        const formGroup = service.createPageLayersFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
