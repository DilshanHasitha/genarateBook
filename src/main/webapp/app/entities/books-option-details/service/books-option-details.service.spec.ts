import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBooksOptionDetails } from '../books-option-details.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../books-option-details.test-samples';

import { BooksOptionDetailsService } from './books-option-details.service';

const requireRestSample: IBooksOptionDetails = {
  ...sampleWithRequiredData,
};

describe('BooksOptionDetails Service', () => {
  let service: BooksOptionDetailsService;
  let httpMock: HttpTestingController;
  let expectedResult: IBooksOptionDetails | IBooksOptionDetails[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BooksOptionDetailsService);
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

    it('should create a BooksOptionDetails', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const booksOptionDetails = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(booksOptionDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BooksOptionDetails', () => {
      const booksOptionDetails = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(booksOptionDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BooksOptionDetails', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BooksOptionDetails', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BooksOptionDetails', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBooksOptionDetailsToCollectionIfMissing', () => {
      it('should add a BooksOptionDetails to an empty array', () => {
        const booksOptionDetails: IBooksOptionDetails = sampleWithRequiredData;
        expectedResult = service.addBooksOptionDetailsToCollectionIfMissing([], booksOptionDetails);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(booksOptionDetails);
      });

      it('should not add a BooksOptionDetails to an array that contains it', () => {
        const booksOptionDetails: IBooksOptionDetails = sampleWithRequiredData;
        const booksOptionDetailsCollection: IBooksOptionDetails[] = [
          {
            ...booksOptionDetails,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBooksOptionDetailsToCollectionIfMissing(booksOptionDetailsCollection, booksOptionDetails);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BooksOptionDetails to an array that doesn't contain it", () => {
        const booksOptionDetails: IBooksOptionDetails = sampleWithRequiredData;
        const booksOptionDetailsCollection: IBooksOptionDetails[] = [sampleWithPartialData];
        expectedResult = service.addBooksOptionDetailsToCollectionIfMissing(booksOptionDetailsCollection, booksOptionDetails);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(booksOptionDetails);
      });

      it('should add only unique BooksOptionDetails to an array', () => {
        const booksOptionDetailsArray: IBooksOptionDetails[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const booksOptionDetailsCollection: IBooksOptionDetails[] = [sampleWithRequiredData];
        expectedResult = service.addBooksOptionDetailsToCollectionIfMissing(booksOptionDetailsCollection, ...booksOptionDetailsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const booksOptionDetails: IBooksOptionDetails = sampleWithRequiredData;
        const booksOptionDetails2: IBooksOptionDetails = sampleWithPartialData;
        expectedResult = service.addBooksOptionDetailsToCollectionIfMissing([], booksOptionDetails, booksOptionDetails2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(booksOptionDetails);
        expect(expectedResult).toContain(booksOptionDetails2);
      });

      it('should accept null and undefined values', () => {
        const booksOptionDetails: IBooksOptionDetails = sampleWithRequiredData;
        expectedResult = service.addBooksOptionDetailsToCollectionIfMissing([], null, booksOptionDetails, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(booksOptionDetails);
      });

      it('should return initial array if no BooksOptionDetails is added', () => {
        const booksOptionDetailsCollection: IBooksOptionDetails[] = [sampleWithRequiredData];
        expectedResult = service.addBooksOptionDetailsToCollectionIfMissing(booksOptionDetailsCollection, undefined, null);
        expect(expectedResult).toEqual(booksOptionDetailsCollection);
      });
    });

    describe('compareBooksOptionDetails', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBooksOptionDetails(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBooksOptionDetails(entity1, entity2);
        const compareResult2 = service.compareBooksOptionDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBooksOptionDetails(entity1, entity2);
        const compareResult2 = service.compareBooksOptionDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBooksOptionDetails(entity1, entity2);
        const compareResult2 = service.compareBooksOptionDetails(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
