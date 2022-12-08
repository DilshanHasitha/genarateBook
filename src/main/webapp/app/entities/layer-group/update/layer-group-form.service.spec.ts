import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../layer-group.test-samples';

import { LayerGroupFormService } from './layer-group-form.service';

describe('LayerGroup Form Service', () => {
  let service: LayerGroupFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LayerGroupFormService);
  });

  describe('Service methods', () => {
    describe('createLayerGroupFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLayerGroupFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            description: expect.any(Object),
            isActive: expect.any(Object),
            layers: expect.any(Object),
            books: expect.any(Object),
          })
        );
      });

      it('passing ILayerGroup should create a new form with FormGroup', () => {
        const formGroup = service.createLayerGroupFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            description: expect.any(Object),
            isActive: expect.any(Object),
            layers: expect.any(Object),
            books: expect.any(Object),
          })
        );
      });
    });

    describe('getLayerGroup', () => {
      it('should return NewLayerGroup for default LayerGroup initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createLayerGroupFormGroup(sampleWithNewData);

        const layerGroup = service.getLayerGroup(formGroup) as any;

        expect(layerGroup).toMatchObject(sampleWithNewData);
      });

      it('should return NewLayerGroup for empty LayerGroup initial value', () => {
        const formGroup = service.createLayerGroupFormGroup();

        const layerGroup = service.getLayerGroup(formGroup) as any;

        expect(layerGroup).toMatchObject({});
      });

      it('should return ILayerGroup', () => {
        const formGroup = service.createLayerGroupFormGroup(sampleWithRequiredData);

        const layerGroup = service.getLayerGroup(formGroup) as any;

        expect(layerGroup).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILayerGroup should not enable id FormControl', () => {
        const formGroup = service.createLayerGroupFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLayerGroup should disable id FormControl', () => {
        const formGroup = service.createLayerGroupFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
