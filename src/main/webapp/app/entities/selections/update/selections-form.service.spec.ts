import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../selections.test-samples';

import { SelectionsFormService } from './selections-form.service';

describe('Selections Form Service', () => {
  let service: SelectionsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SelectionsFormService);
  });

  describe('Service methods', () => {
    describe('createSelectionsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSelectionsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            avatarCode: expect.any(Object),
            styleCode: expect.any(Object),
            optionCode: expect.any(Object),
            image: expect.any(Object),
            height: expect.any(Object),
            x: expect.any(Object),
            y: expect.any(Object),
            isActive: expect.any(Object),
            width: expect.any(Object),
            avatarAttributesCode: expect.any(Object),
            avatarStyle: expect.any(Object),
          })
        );
      });

      it('passing ISelections should create a new form with FormGroup', () => {
        const formGroup = service.createSelectionsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            avatarCode: expect.any(Object),
            styleCode: expect.any(Object),
            optionCode: expect.any(Object),
            image: expect.any(Object),
            height: expect.any(Object),
            x: expect.any(Object),
            y: expect.any(Object),
            isActive: expect.any(Object),
            width: expect.any(Object),
            avatarAttributesCode: expect.any(Object),
            avatarStyle: expect.any(Object),
          })
        );
      });
    });

    describe('getSelections', () => {
      it('should return NewSelections for default Selections initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSelectionsFormGroup(sampleWithNewData);

        const selections = service.getSelections(formGroup) as any;

        expect(selections).toMatchObject(sampleWithNewData);
      });

      it('should return NewSelections for empty Selections initial value', () => {
        const formGroup = service.createSelectionsFormGroup();

        const selections = service.getSelections(formGroup) as any;

        expect(selections).toMatchObject({});
      });

      it('should return ISelections', () => {
        const formGroup = service.createSelectionsFormGroup(sampleWithRequiredData);

        const selections = service.getSelections(formGroup) as any;

        expect(selections).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISelections should not enable id FormControl', () => {
        const formGroup = service.createSelectionsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSelections should disable id FormControl', () => {
        const formGroup = service.createSelectionsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
