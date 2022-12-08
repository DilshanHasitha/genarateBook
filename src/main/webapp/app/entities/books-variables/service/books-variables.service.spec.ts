import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBooksVariables } from '../books-variables.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../books-variables.test-samples';

import { BooksVariablesService } from './books-variables.service';

const requireRestSample: IBooksVariables = {
  ...sampleWithRequiredData,
};

describe('BooksVariables Service', () => {
  let service: BooksVariablesService;
  let httpMock: HttpTestingController;
  let expectedResult: IBooksVariables | IBooksVariables[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BooksVariablesService);
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

    it('should create a BooksVariables', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const booksVariables = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(booksVariables).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BooksVariables', () => {
      const booksVariables = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(booksVariables).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BooksVariables', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BooksVariables', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BooksVariables', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBooksVariablesToCollectionIfMissing', () => {
      it('should add a BooksVariables to an empty array', () => {
        const booksVariables: IBooksVariables = sampleWithRequiredData;
        expectedResult = service.addBooksVariablesToCollectionIfMissing([], booksVariables);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(booksVariables);
      });

      it('should not add a BooksVariables to an array that contains it', () => {
        const booksVariables: IBooksVariables = sampleWithRequiredData;
        const booksVariablesCollection: IBooksVariables[] = [
          {
            ...booksVariables,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBooksVariablesToCollectionIfMissing(booksVariablesCollection, booksVariables);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BooksVariables to an array that doesn't contain it", () => {
        const booksVariables: IBooksVariables = sampleWithRequiredData;
        const booksVariablesCollection: IBooksVariables[] = [sampleWithPartialData];
        expectedResult = service.addBooksVariablesToCollectionIfMissing(booksVariablesCollection, booksVariables);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(booksVariables);
      });

      it('should add only unique BooksVariables to an array', () => {
        const booksVariablesArray: IBooksVariables[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const booksVariablesCollection: IBooksVariables[] = [sampleWithRequiredData];
        expectedResult = service.addBooksVariablesToCollectionIfMissing(booksVariablesCollection, ...booksVariablesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const booksVariables: IBooksVariables = sampleWithRequiredData;
        const booksVariables2: IBooksVariables = sampleWithPartialData;
        expectedResult = service.addBooksVariablesToCollectionIfMissing([], booksVariables, booksVariables2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(booksVariables);
        expect(expectedResult).toContain(booksVariables2);
      });

      it('should accept null and undefined values', () => {
        const booksVariables: IBooksVariables = sampleWithRequiredData;
        expectedResult = service.addBooksVariablesToCollectionIfMissing([], null, booksVariables, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(booksVariables);
      });

      it('should return initial array if no BooksVariables is added', () => {
        const booksVariablesCollection: IBooksVariables[] = [sampleWithRequiredData];
        expectedResult = service.addBooksVariablesToCollectionIfMissing(booksVariablesCollection, undefined, null);
        expect(expectedResult).toEqual(booksVariablesCollection);
      });
    });

    describe('compareBooksVariables', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBooksVariables(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBooksVariables(entity1, entity2);
        const compareResult2 = service.compareBooksVariables(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBooksVariables(entity1, entity2);
        const compareResult2 = service.compareBooksVariables(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBooksVariables(entity1, entity2);
        const compareResult2 = service.compareBooksVariables(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
