import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBooksRelatedOptionDetails } from '../books-related-option-details.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../books-related-option-details.test-samples';

import { BooksRelatedOptionDetailsService } from './books-related-option-details.service';

const requireRestSample: IBooksRelatedOptionDetails = {
  ...sampleWithRequiredData,
};

describe('BooksRelatedOptionDetails Service', () => {
  let service: BooksRelatedOptionDetailsService;
  let httpMock: HttpTestingController;
  let expectedResult: IBooksRelatedOptionDetails | IBooksRelatedOptionDetails[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BooksRelatedOptionDetailsService);
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

    it('should create a BooksRelatedOptionDetails', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const booksRelatedOptionDetails = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(booksRelatedOptionDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BooksRelatedOptionDetails', () => {
      const booksRelatedOptionDetails = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(booksRelatedOptionDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BooksRelatedOptionDetails', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BooksRelatedOptionDetails', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BooksRelatedOptionDetails', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBooksRelatedOptionDetailsToCollectionIfMissing', () => {
      it('should add a BooksRelatedOptionDetails to an empty array', () => {
        const booksRelatedOptionDetails: IBooksRelatedOptionDetails = sampleWithRequiredData;
        expectedResult = service.addBooksRelatedOptionDetailsToCollectionIfMissing([], booksRelatedOptionDetails);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(booksRelatedOptionDetails);
      });

      it('should not add a BooksRelatedOptionDetails to an array that contains it', () => {
        const booksRelatedOptionDetails: IBooksRelatedOptionDetails = sampleWithRequiredData;
        const booksRelatedOptionDetailsCollection: IBooksRelatedOptionDetails[] = [
          {
            ...booksRelatedOptionDetails,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBooksRelatedOptionDetailsToCollectionIfMissing(
          booksRelatedOptionDetailsCollection,
          booksRelatedOptionDetails
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BooksRelatedOptionDetails to an array that doesn't contain it", () => {
        const booksRelatedOptionDetails: IBooksRelatedOptionDetails = sampleWithRequiredData;
        const booksRelatedOptionDetailsCollection: IBooksRelatedOptionDetails[] = [sampleWithPartialData];
        expectedResult = service.addBooksRelatedOptionDetailsToCollectionIfMissing(
          booksRelatedOptionDetailsCollection,
          booksRelatedOptionDetails
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(booksRelatedOptionDetails);
      });

      it('should add only unique BooksRelatedOptionDetails to an array', () => {
        const booksRelatedOptionDetailsArray: IBooksRelatedOptionDetails[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const booksRelatedOptionDetailsCollection: IBooksRelatedOptionDetails[] = [sampleWithRequiredData];
        expectedResult = service.addBooksRelatedOptionDetailsToCollectionIfMissing(
          booksRelatedOptionDetailsCollection,
          ...booksRelatedOptionDetailsArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const booksRelatedOptionDetails: IBooksRelatedOptionDetails = sampleWithRequiredData;
        const booksRelatedOptionDetails2: IBooksRelatedOptionDetails = sampleWithPartialData;
        expectedResult = service.addBooksRelatedOptionDetailsToCollectionIfMissing(
          [],
          booksRelatedOptionDetails,
          booksRelatedOptionDetails2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(booksRelatedOptionDetails);
        expect(expectedResult).toContain(booksRelatedOptionDetails2);
      });

      it('should accept null and undefined values', () => {
        const booksRelatedOptionDetails: IBooksRelatedOptionDetails = sampleWithRequiredData;
        expectedResult = service.addBooksRelatedOptionDetailsToCollectionIfMissing([], null, booksRelatedOptionDetails, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(booksRelatedOptionDetails);
      });

      it('should return initial array if no BooksRelatedOptionDetails is added', () => {
        const booksRelatedOptionDetailsCollection: IBooksRelatedOptionDetails[] = [sampleWithRequiredData];
        expectedResult = service.addBooksRelatedOptionDetailsToCollectionIfMissing(booksRelatedOptionDetailsCollection, undefined, null);
        expect(expectedResult).toEqual(booksRelatedOptionDetailsCollection);
      });
    });

    describe('compareBooksRelatedOptionDetails', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBooksRelatedOptionDetails(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBooksRelatedOptionDetails(entity1, entity2);
        const compareResult2 = service.compareBooksRelatedOptionDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBooksRelatedOptionDetails(entity1, entity2);
        const compareResult2 = service.compareBooksRelatedOptionDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBooksRelatedOptionDetails(entity1, entity2);
        const compareResult2 = service.compareBooksRelatedOptionDetails(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
