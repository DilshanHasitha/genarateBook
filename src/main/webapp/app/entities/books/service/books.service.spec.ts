import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBooks } from '../books.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../books.test-samples';

import { BooksService } from './books.service';

const requireRestSample: IBooks = {
  ...sampleWithRequiredData,
};

describe('Books Service', () => {
  let service: BooksService;
  let httpMock: HttpTestingController;
  let expectedResult: IBooks | IBooks[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BooksService);
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

    it('should create a Books', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const books = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(books).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Books', () => {
      const books = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(books).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Books', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Books', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Books', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBooksToCollectionIfMissing', () => {
      it('should add a Books to an empty array', () => {
        const books: IBooks = sampleWithRequiredData;
        expectedResult = service.addBooksToCollectionIfMissing([], books);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(books);
      });

      it('should not add a Books to an array that contains it', () => {
        const books: IBooks = sampleWithRequiredData;
        const booksCollection: IBooks[] = [
          {
            ...books,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBooksToCollectionIfMissing(booksCollection, books);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Books to an array that doesn't contain it", () => {
        const books: IBooks = sampleWithRequiredData;
        const booksCollection: IBooks[] = [sampleWithPartialData];
        expectedResult = service.addBooksToCollectionIfMissing(booksCollection, books);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(books);
      });

      it('should add only unique Books to an array', () => {
        const booksArray: IBooks[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const booksCollection: IBooks[] = [sampleWithRequiredData];
        expectedResult = service.addBooksToCollectionIfMissing(booksCollection, ...booksArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const books: IBooks = sampleWithRequiredData;
        const books2: IBooks = sampleWithPartialData;
        expectedResult = service.addBooksToCollectionIfMissing([], books, books2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(books);
        expect(expectedResult).toContain(books2);
      });

      it('should accept null and undefined values', () => {
        const books: IBooks = sampleWithRequiredData;
        expectedResult = service.addBooksToCollectionIfMissing([], null, books, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(books);
      });

      it('should return initial array if no Books is added', () => {
        const booksCollection: IBooks[] = [sampleWithRequiredData];
        expectedResult = service.addBooksToCollectionIfMissing(booksCollection, undefined, null);
        expect(expectedResult).toEqual(booksCollection);
      });
    });

    describe('compareBooks', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBooks(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBooks(entity1, entity2);
        const compareResult2 = service.compareBooks(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBooks(entity1, entity2);
        const compareResult2 = service.compareBooks(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBooks(entity1, entity2);
        const compareResult2 = service.compareBooks(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
