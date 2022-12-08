import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../books-option-details.test-samples';

import { BooksOptionDetailsFormService } from './books-option-details-form.service';

describe('BooksOptionDetails Form Service', () => {
  let service: BooksOptionDetailsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BooksOptionDetailsFormService);
  });

  describe('Service methods', () => {
    describe('createBooksOptionDetailsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBooksOptionDetailsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            avatarAttributes: expect.any(Object),
            avatarCharactor: expect.any(Object),
            style: expect.any(Object),
            option: expect.any(Object),
            isActive: expect.any(Object),
            books: expect.any(Object),
          })
        );
      });

      it('passing IBooksOptionDetails should create a new form with FormGroup', () => {
        const formGroup = service.createBooksOptionDetailsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            avatarAttributes: expect.any(Object),
            avatarCharactor: expect.any(Object),
            style: expect.any(Object),
            option: expect.any(Object),
            isActive: expect.any(Object),
            books: expect.any(Object),
          })
        );
      });
    });

    describe('getBooksOptionDetails', () => {
      it('should return NewBooksOptionDetails for default BooksOptionDetails initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createBooksOptionDetailsFormGroup(sampleWithNewData);

        const booksOptionDetails = service.getBooksOptionDetails(formGroup) as any;

        expect(booksOptionDetails).toMatchObject(sampleWithNewData);
      });

      it('should return NewBooksOptionDetails for empty BooksOptionDetails initial value', () => {
        const formGroup = service.createBooksOptionDetailsFormGroup();

        const booksOptionDetails = service.getBooksOptionDetails(formGroup) as any;

        expect(booksOptionDetails).toMatchObject({});
      });

      it('should return IBooksOptionDetails', () => {
        const formGroup = service.createBooksOptionDetailsFormGroup(sampleWithRequiredData);

        const booksOptionDetails = service.getBooksOptionDetails(formGroup) as any;

        expect(booksOptionDetails).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBooksOptionDetails should not enable id FormControl', () => {
        const formGroup = service.createBooksOptionDetailsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBooksOptionDetails should disable id FormControl', () => {
        const formGroup = service.createBooksOptionDetailsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
