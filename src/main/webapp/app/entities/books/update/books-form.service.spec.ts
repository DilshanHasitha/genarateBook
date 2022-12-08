import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../books.test-samples';

import { BooksFormService } from './books-form.service';

describe('Books Form Service', () => {
  let service: BooksFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BooksFormService);
  });

  describe('Service methods', () => {
    describe('createBooksFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBooksFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            title: expect.any(Object),
            subTitle: expect.any(Object),
            author: expect.any(Object),
            isActive: expect.any(Object),
            noOfPages: expect.any(Object),
            storeImg: expect.any(Object),
            pageSize: expect.any(Object),
            user: expect.any(Object),
            booksPages: expect.any(Object),
            priceRelatedOptions: expect.any(Object),
            booksRelatedOptions: expect.any(Object),
            booksAttributes: expect.any(Object),
            booksVariables: expect.any(Object),
            avatarAttributes: expect.any(Object),
            layerGroups: expect.any(Object),
          })
        );
      });

      it('passing IBooks should create a new form with FormGroup', () => {
        const formGroup = service.createBooksFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            title: expect.any(Object),
            subTitle: expect.any(Object),
            author: expect.any(Object),
            isActive: expect.any(Object),
            noOfPages: expect.any(Object),
            storeImg: expect.any(Object),
            pageSize: expect.any(Object),
            user: expect.any(Object),
            booksPages: expect.any(Object),
            priceRelatedOptions: expect.any(Object),
            booksRelatedOptions: expect.any(Object),
            booksAttributes: expect.any(Object),
            booksVariables: expect.any(Object),
            avatarAttributes: expect.any(Object),
            layerGroups: expect.any(Object),
          })
        );
      });
    });

    describe('getBooks', () => {
      it('should return NewBooks for default Books initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createBooksFormGroup(sampleWithNewData);

        const books = service.getBooks(formGroup) as any;

        expect(books).toMatchObject(sampleWithNewData);
      });

      it('should return NewBooks for empty Books initial value', () => {
        const formGroup = service.createBooksFormGroup();

        const books = service.getBooks(formGroup) as any;

        expect(books).toMatchObject({});
      });

      it('should return IBooks', () => {
        const formGroup = service.createBooksFormGroup(sampleWithRequiredData);

        const books = service.getBooks(formGroup) as any;

        expect(books).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBooks should not enable id FormControl', () => {
        const formGroup = service.createBooksFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBooks should disable id FormControl', () => {
        const formGroup = service.createBooksFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
