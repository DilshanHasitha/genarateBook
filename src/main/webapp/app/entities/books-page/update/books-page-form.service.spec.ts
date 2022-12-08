import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../books-page.test-samples';

import { BooksPageFormService } from './books-page-form.service';

describe('BooksPage Form Service', () => {
  let service: BooksPageFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BooksPageFormService);
  });

  describe('Service methods', () => {
    describe('createBooksPageFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBooksPageFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            num: expect.any(Object),
            isActive: expect.any(Object),
            pageDetails: expect.any(Object),
            books: expect.any(Object),
          })
        );
      });

      it('passing IBooksPage should create a new form with FormGroup', () => {
        const formGroup = service.createBooksPageFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            num: expect.any(Object),
            isActive: expect.any(Object),
            pageDetails: expect.any(Object),
            books: expect.any(Object),
          })
        );
      });
    });

    describe('getBooksPage', () => {
      it('should return NewBooksPage for default BooksPage initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createBooksPageFormGroup(sampleWithNewData);

        const booksPage = service.getBooksPage(formGroup) as any;

        expect(booksPage).toMatchObject(sampleWithNewData);
      });

      it('should return NewBooksPage for empty BooksPage initial value', () => {
        const formGroup = service.createBooksPageFormGroup();

        const booksPage = service.getBooksPage(formGroup) as any;

        expect(booksPage).toMatchObject({});
      });

      it('should return IBooksPage', () => {
        const formGroup = service.createBooksPageFormGroup(sampleWithRequiredData);

        const booksPage = service.getBooksPage(formGroup) as any;

        expect(booksPage).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBooksPage should not enable id FormControl', () => {
        const formGroup = service.createBooksPageFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBooksPage should disable id FormControl', () => {
        const formGroup = service.createBooksPageFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
