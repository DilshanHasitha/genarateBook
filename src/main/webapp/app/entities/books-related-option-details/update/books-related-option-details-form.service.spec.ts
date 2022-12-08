import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../books-related-option-details.test-samples';

import { BooksRelatedOptionDetailsFormService } from './books-related-option-details-form.service';

describe('BooksRelatedOptionDetails Form Service', () => {
  let service: BooksRelatedOptionDetailsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BooksRelatedOptionDetailsFormService);
  });

  describe('Service methods', () => {
    describe('createBooksRelatedOptionDetailsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBooksRelatedOptionDetailsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            description: expect.any(Object),
            isActive: expect.any(Object),
            booksRelatedOptions: expect.any(Object),
          })
        );
      });

      it('passing IBooksRelatedOptionDetails should create a new form with FormGroup', () => {
        const formGroup = service.createBooksRelatedOptionDetailsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            description: expect.any(Object),
            isActive: expect.any(Object),
            booksRelatedOptions: expect.any(Object),
          })
        );
      });
    });

    describe('getBooksRelatedOptionDetails', () => {
      it('should return NewBooksRelatedOptionDetails for default BooksRelatedOptionDetails initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createBooksRelatedOptionDetailsFormGroup(sampleWithNewData);

        const booksRelatedOptionDetails = service.getBooksRelatedOptionDetails(formGroup) as any;

        expect(booksRelatedOptionDetails).toMatchObject(sampleWithNewData);
      });

      it('should return NewBooksRelatedOptionDetails for empty BooksRelatedOptionDetails initial value', () => {
        const formGroup = service.createBooksRelatedOptionDetailsFormGroup();

        const booksRelatedOptionDetails = service.getBooksRelatedOptionDetails(formGroup) as any;

        expect(booksRelatedOptionDetails).toMatchObject({});
      });

      it('should return IBooksRelatedOptionDetails', () => {
        const formGroup = service.createBooksRelatedOptionDetailsFormGroup(sampleWithRequiredData);

        const booksRelatedOptionDetails = service.getBooksRelatedOptionDetails(formGroup) as any;

        expect(booksRelatedOptionDetails).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBooksRelatedOptionDetails should not enable id FormControl', () => {
        const formGroup = service.createBooksRelatedOptionDetailsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBooksRelatedOptionDetails should disable id FormControl', () => {
        const formGroup = service.createBooksRelatedOptionDetailsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
