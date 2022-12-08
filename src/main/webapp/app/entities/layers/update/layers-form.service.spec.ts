import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../layers.test-samples';

import { LayersFormService } from './layers-form.service';

describe('Layers Form Service', () => {
  let service: LayersFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LayersFormService);
  });

  describe('Service methods', () => {
    describe('createLayersFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLayersFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            layerNo: expect.any(Object),
            isActive: expect.any(Object),
            layerdetails: expect.any(Object),
            layerGroups: expect.any(Object),
          })
        );
      });

      it('passing ILayers should create a new form with FormGroup', () => {
        const formGroup = service.createLayersFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            layerNo: expect.any(Object),
            isActive: expect.any(Object),
            layerdetails: expect.any(Object),
            layerGroups: expect.any(Object),
          })
        );
      });
    });

    describe('getLayers', () => {
      it('should return NewLayers for default Layers initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createLayersFormGroup(sampleWithNewData);

        const layers = service.getLayers(formGroup) as any;

        expect(layers).toMatchObject(sampleWithNewData);
      });

      it('should return NewLayers for empty Layers initial value', () => {
        const formGroup = service.createLayersFormGroup();

        const layers = service.getLayers(formGroup) as any;

        expect(layers).toMatchObject({});
      });

      it('should return ILayers', () => {
        const formGroup = service.createLayersFormGroup(sampleWithRequiredData);

        const layers = service.getLayers(formGroup) as any;

        expect(layers).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILayers should not enable id FormControl', () => {
        const formGroup = service.createLayersFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLayers should disable id FormControl', () => {
        const formGroup = service.createLayersFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
