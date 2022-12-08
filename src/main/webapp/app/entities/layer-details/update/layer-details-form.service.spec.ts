import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../layer-details.test-samples';

import { LayerDetailsFormService } from './layer-details-form.service';

describe('LayerDetails Form Service', () => {
  let service: LayerDetailsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LayerDetailsFormService);
  });

  describe('Service methods', () => {
    describe('createLayerDetailsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLayerDetailsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            layers: expect.any(Object),
          })
        );
      });

      it('passing ILayerDetails should create a new form with FormGroup', () => {
        const formGroup = service.createLayerDetailsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            layers: expect.any(Object),
          })
        );
      });
    });

    describe('getLayerDetails', () => {
      it('should return NewLayerDetails for default LayerDetails initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createLayerDetailsFormGroup(sampleWithNewData);

        const layerDetails = service.getLayerDetails(formGroup) as any;

        expect(layerDetails).toMatchObject(sampleWithNewData);
      });

      it('should return NewLayerDetails for empty LayerDetails initial value', () => {
        const formGroup = service.createLayerDetailsFormGroup();

        const layerDetails = service.getLayerDetails(formGroup) as any;

        expect(layerDetails).toMatchObject({});
      });

      it('should return ILayerDetails', () => {
        const formGroup = service.createLayerDetailsFormGroup(sampleWithRequiredData);

        const layerDetails = service.getLayerDetails(formGroup) as any;

        expect(layerDetails).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILayerDetails should not enable id FormControl', () => {
        const formGroup = service.createLayerDetailsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLayerDetails should disable id FormControl', () => {
        const formGroup = service.createLayerDetailsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
