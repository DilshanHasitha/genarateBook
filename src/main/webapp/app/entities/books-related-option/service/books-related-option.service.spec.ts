import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBooksRelatedOption } from '../books-related-option.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../books-related-option.test-samples';

import { BooksRelatedOptionService } from './books-related-option.service';

const requireRestSample: IBooksRelatedOption = {
  ...sampleWithRequiredData,
};

describe('BooksRelatedOption Service', () => {
  let service: BooksRelatedOptionService;
  let httpMock: HttpTestingController;
  let expectedResult: IBooksRelatedOption | IBooksRelatedOption[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BooksRelatedOptionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a BooksRelatedOption', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const booksRelatedOption = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(booksRelatedOption).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BooksRelatedOption', () => {
      const booksRelatedOption = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(booksRelatedOption).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BooksRelatedOption', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BooksRelatedOption', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BooksRelatedOption', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBooksRelatedOptionToCollectionIfMissing', () => {
      it('should add a BooksRelatedOption to an empty array', () => {
        const booksRelatedOption: IBooksRelatedOption = sampleWithRequiredData;
        expectedResult = service.addBooksRelatedOptionToCollectionIfMissing([], booksRelatedOption);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(booksRelatedOption);
      });

      it('should not add a BooksRelatedOption to an array that contains it', () => {
        const booksRelatedOption: IBooksRelatedOption = sampleWithRequiredData;
        const booksRelatedOptionCollection: IBooksRelatedOption[] = [
          {
            ...booksRelatedOption,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBooksRelatedOptionToCollectionIfMissing(booksRelatedOptionCollection, booksRelatedOption);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BooksRelatedOption to an array that doesn't contain it", () => {
        const booksRelatedOption: IBooksRelatedOption = sampleWithRequiredData;
        const booksRelatedOptionCollection: IBooksRelatedOption[] = [sampleWithPartialData];
        expectedResult = service.addBooksRelatedOptionToCollectionIfMissing(booksRelatedOptionCollection, booksRelatedOption);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(booksRelatedOption);
      });

      it('should add only unique BooksRelatedOption to an array', () => {
        const booksRelatedOptionArray: IBooksRelatedOption[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const booksRelatedOptionCollection: IBooksRelatedOption[] = [sampleWithRequiredData];
        expectedResult = service.addBooksRelatedOptionToCollectionIfMissing(booksRelatedOptionCollection, ...booksRelatedOptionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const booksRelatedOption: IBooksRelatedOption = sampleWithRequiredData;
        const booksRelatedOption2: IBooksRelatedOption = sampleWithPartialData;
        expectedResult = service.addBooksRelatedOptionToCollectionIfMissing([], booksRelatedOption, booksRelatedOption2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(booksRelatedOption);
        expect(expectedResult).toContain(booksRelatedOption2);
      });

      it('should accept null and undefined values', () => {
        const booksRelatedOption: IBooksRelatedOption = sampleWithRequiredData;
        expectedResult = service.addBooksRelatedOptionToCollectionIfMissing([], null, booksRelatedOption, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(booksRelatedOption);
      });

      it('should return initial array if no BooksRelatedOption is added', () => {
        const booksRelatedOptionCollection: IBooksRelatedOption[] = [sampleWithRequiredData];
        expectedResult = service.addBooksRelatedOptionToCollectionIfMissing(booksRelatedOptionCollection, undefined, null);
        expect(expectedResult).toEqual(booksRelatedOptionCollection);
      });
    });

    describe('compareBooksRelatedOption', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBooksRelatedOption(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBooksRelatedOption(entity1, entity2);
        const compareResult2 = service.compareBooksRelatedOption(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBooksRelatedOption(entity1, entity2);
        const compareResult2 = service.compareBooksRelatedOption(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBooksRelatedOption(entity1, entity2);
        const compareResult2 = service.compareBooksRelatedOption(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
