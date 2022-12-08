import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../books-variables.test-samples';

import { BooksVariablesFormService } from './books-variables-form.service';

describe('BooksVariables Form Service', () => {
  let service: BooksVariablesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BooksVariablesFormService);
  });

  describe('Service methods', () => {
    describe('createBooksVariablesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBooksVariablesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            description: expect.any(Object),
            isActive: expect.any(Object),
            books: expect.any(Object),
          })
        );
      });

      it('passing IBooksVariables should create a new form with FormGroup', () => {
        const formGroup = service.createBooksVariablesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            description: expect.any(Object),
            isActive: expect.any(Object),
            books: expect.any(Object),
          })
        );
      });
    });

    describe('getBooksVariables', () => {
      it('should return NewBooksVariables for default BooksVariables initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createBooksVariablesFormGroup(sampleWithNewData);

        const booksVariables = service.getBooksVariables(formGroup) as any;

        expect(booksVariables).toMatchObject(sampleWithNewData);
      });

      it('should return NewBooksVariables for empty BooksVariables initial value', () => {
        const formGroup = service.createBooksVariablesFormGroup();

        const booksVariables = service.getBooksVariables(formGroup) as any;

        expect(booksVariables).toMatchObject({});
      });

      it('should return IBooksVariables', () => {
        const formGroup = service.createBooksVariablesFormGroup(sampleWithRequiredData);

        const booksVariables = service.getBooksVariables(formGroup) as any;

        expect(booksVariables).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBooksVariables should not enable id FormControl', () => {
        const formGroup = service.createBooksVariablesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBooksVariables should disable id FormControl', () => {
        const formGroup = service.createBooksVariablesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
