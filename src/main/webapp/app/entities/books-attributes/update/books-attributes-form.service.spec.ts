import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../books-attributes.test-samples';

import { BooksAttributesFormService } from './books-attributes-form.service';

describe('BooksAttributes Form Service', () => {
  let service: BooksAttributesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BooksAttributesFormService);
  });

  describe('Service methods', () => {
    describe('createBooksAttributesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBooksAttributesFormGroup();

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

      it('passing IBooksAttributes should create a new form with FormGroup', () => {
        const formGroup = service.createBooksAttributesFormGroup(sampleWithRequiredData);

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

    describe('getBooksAttributes', () => {
      it('should return NewBooksAttributes for default BooksAttributes initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createBooksAttributesFormGroup(sampleWithNewData);

        const booksAttributes = service.getBooksAttributes(formGroup) as any;

        expect(booksAttributes).toMatchObject(sampleWithNewData);
      });

      it('should return NewBooksAttributes for empty BooksAttributes initial value', () => {
        const formGroup = service.createBooksAttributesFormGroup();

        const booksAttributes = service.getBooksAttributes(formGroup) as any;

        expect(booksAttributes).toMatchObject({});
      });

      it('should return IBooksAttributes', () => {
        const formGroup = service.createBooksAttributesFormGroup(sampleWithRequiredData);

        const booksAttributes = service.getBooksAttributes(formGroup) as any;

        expect(booksAttributes).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBooksAttributes should not enable id FormControl', () => {
        const formGroup = service.createBooksAttributesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBooksAttributes should disable id FormControl', () => {
        const formGroup = service.createBooksAttributesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
