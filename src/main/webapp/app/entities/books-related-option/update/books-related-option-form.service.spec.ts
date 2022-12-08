import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../books-related-option.test-samples';

import { BooksRelatedOptionFormService } from './books-related-option-form.service';

describe('BooksRelatedOption Form Service', () => {
  let service: BooksRelatedOptionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BooksRelatedOptionFormService);
  });

  describe('Service methods', () => {
    describe('createBooksRelatedOptionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBooksRelatedOptionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            isActive: expect.any(Object),
            booksRelatedOptionDetails: expect.any(Object),
            books: expect.any(Object),
          })
        );
      });

      it('passing IBooksRelatedOption should create a new form with FormGroup', () => {
        const formGroup = service.createBooksRelatedOptionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            isActive: expect.any(Object),
            booksRelatedOptionDetails: expect.any(Object),
            books: expect.any(Object),
          })
        );
      });
    });

    describe('getBooksRelatedOption', () => {
      it('should return NewBooksRelatedOption for default BooksRelatedOption initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createBooksRelatedOptionFormGroup(sampleWithNewData);

        const booksRelatedOption = service.getBooksRelatedOption(formGroup) as any;

        expect(booksRelatedOption).toMatchObject(sampleWithNewData);
      });

      it('should return NewBooksRelatedOption for empty BooksRelatedOption initial value', () => {
        const formGroup = service.createBooksRelatedOptionFormGroup();

        const booksRelatedOption = service.getBooksRelatedOption(formGroup) as any;

        expect(booksRelatedOption).toMatchObject({});
      });

      it('should return IBooksRelatedOption', () => {
        const formGroup = service.createBooksRelatedOptionFormGroup(sampleWithRequiredData);

        const booksRelatedOption = service.getBooksRelatedOption(formGroup) as any;

        expect(booksRelatedOption).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBooksRelatedOption should not enable id FormControl', () => {
        const formGroup = service.createBooksRelatedOptionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBooksRelatedOption should disable id FormControl', () => {
        const formGroup = service.createBooksRelatedOptionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
