import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBooksAttributes } from '../books-attributes.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../books-attributes.test-samples';

import { BooksAttributesService } from './books-attributes.service';

const requireRestSample: IBooksAttributes = {
  ...sampleWithRequiredData,
};

describe('BooksAttributes Service', () => {
  let service: BooksAttributesService;
  let httpMock: HttpTestingController;
  let expectedResult: IBooksAttributes | IBooksAttributes[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BooksAttributesService);
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

    it('should create a BooksAttributes', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const booksAttributes = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(booksAttributes).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BooksAttributes', () => {
      const booksAttributes = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(booksAttributes).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BooksAttributes', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BooksAttributes', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BooksAttributes', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBooksAttributesToCollectionIfMissing', () => {
      it('should add a BooksAttributes to an empty array', () => {
        const booksAttributes: IBooksAttributes = sampleWithRequiredData;
        expectedResult = service.addBooksAttributesToCollectionIfMissing([], booksAttributes);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(booksAttributes);
      });

      it('should not add a BooksAttributes to an array that contains it', () => {
        const booksAttributes: IBooksAttributes = sampleWithRequiredData;
        const booksAttributesCollection: IBooksAttributes[] = [
          {
            ...booksAttributes,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBooksAttributesToCollectionIfMissing(booksAttributesCollection, booksAttributes);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BooksAttributes to an array that doesn't contain it", () => {
        const booksAttributes: IBooksAttributes = sampleWithRequiredData;
        const booksAttributesCollection: IBooksAttributes[] = [sampleWithPartialData];
        expectedResult = service.addBooksAttributesToCollectionIfMissing(booksAttributesCollection, booksAttributes);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(booksAttributes);
      });

      it('should add only unique BooksAttributes to an array', () => {
        const booksAttributesArray: IBooksAttributes[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const booksAttributesCollection: IBooksAttributes[] = [sampleWithRequiredData];
        expectedResult = service.addBooksAttributesToCollectionIfMissing(booksAttributesCollection, ...booksAttributesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const booksAttributes: IBooksAttributes = sampleWithRequiredData;
        const booksAttributes2: IBooksAttributes = sampleWithPartialData;
        expectedResult = service.addBooksAttributesToCollectionIfMissing([], booksAttributes, booksAttributes2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(booksAttributes);
        expect(expectedResult).toContain(booksAttributes2);
      });

      it('should accept null and undefined values', () => {
        const booksAttributes: IBooksAttributes = sampleWithRequiredData;
        expectedResult = service.addBooksAttributesToCollectionIfMissing([], null, booksAttributes, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(booksAttributes);
      });

      it('should return initial array if no BooksAttributes is added', () => {
        const booksAttributesCollection: IBooksAttributes[] = [sampleWithRequiredData];
        expectedResult = service.addBooksAttributesToCollectionIfMissing(booksAttributesCollection, undefined, null);
        expect(expectedResult).toEqual(booksAttributesCollection);
      });
    });

    describe('compareBooksAttributes', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBooksAttributes(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBooksAttributes(entity1, entity2);
        const compareResult2 = service.compareBooksAttributes(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBooksAttributes(entity1, entity2);
        const compareResult2 = service.compareBooksAttributes(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBooksAttributes(entity1, entity2);
        const compareResult2 = service.compareBooksAttributes(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
