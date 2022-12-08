import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPageLayersDetails } from '../page-layers-details.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../page-layers-details.test-samples';

import { PageLayersDetailsService } from './page-layers-details.service';

const requireRestSample: IPageLayersDetails = {
  ...sampleWithRequiredData,
};

describe('PageLayersDetails Service', () => {
  let service: PageLayersDetailsService;
  let httpMock: HttpTestingController;
  let expectedResult: IPageLayersDetails | IPageLayersDetails[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PageLayersDetailsService);
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

    it('should create a PageLayersDetails', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const pageLayersDetails = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pageLayersDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PageLayersDetails', () => {
      const pageLayersDetails = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pageLayersDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PageLayersDetails', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PageLayersDetails', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PageLayersDetails', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPageLayersDetailsToCollectionIfMissing', () => {
      it('should add a PageLayersDetails to an empty array', () => {
        const pageLayersDetails: IPageLayersDetails = sampleWithRequiredData;
        expectedResult = service.addPageLayersDetailsToCollectionIfMissing([], pageLayersDetails);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pageLayersDetails);
      });

      it('should not add a PageLayersDetails to an array that contains it', () => {
        const pageLayersDetails: IPageLayersDetails = sampleWithRequiredData;
        const pageLayersDetailsCollection: IPageLayersDetails[] = [
          {
            ...pageLayersDetails,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPageLayersDetailsToCollectionIfMissing(pageLayersDetailsCollection, pageLayersDetails);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PageLayersDetails to an array that doesn't contain it", () => {
        const pageLayersDetails: IPageLayersDetails = sampleWithRequiredData;
        const pageLayersDetailsCollection: IPageLayersDetails[] = [sampleWithPartialData];
        expectedResult = service.addPageLayersDetailsToCollectionIfMissing(pageLayersDetailsCollection, pageLayersDetails);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pageLayersDetails);
      });

      it('should add only unique PageLayersDetails to an array', () => {
        const pageLayersDetailsArray: IPageLayersDetails[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pageLayersDetailsCollection: IPageLayersDetails[] = [sampleWithRequiredData];
        expectedResult = service.addPageLayersDetailsToCollectionIfMissing(pageLayersDetailsCollection, ...pageLayersDetailsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pageLayersDetails: IPageLayersDetails = sampleWithRequiredData;
        const pageLayersDetails2: IPageLayersDetails = sampleWithPartialData;
        expectedResult = service.addPageLayersDetailsToCollectionIfMissing([], pageLayersDetails, pageLayersDetails2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pageLayersDetails);
        expect(expectedResult).toContain(pageLayersDetails2);
      });

      it('should accept null and undefined values', () => {
        const pageLayersDetails: IPageLayersDetails = sampleWithRequiredData;
        expectedResult = service.addPageLayersDetailsToCollectionIfMissing([], null, pageLayersDetails, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pageLayersDetails);
      });

      it('should return initial array if no PageLayersDetails is added', () => {
        const pageLayersDetailsCollection: IPageLayersDetails[] = [sampleWithRequiredData];
        expectedResult = service.addPageLayersDetailsToCollectionIfMissing(pageLayersDetailsCollection, undefined, null);
        expect(expectedResult).toEqual(pageLayersDetailsCollection);
      });
    });

    describe('comparePageLayersDetails', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePageLayersDetails(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePageLayersDetails(entity1, entity2);
        const compareResult2 = service.comparePageLayersDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePageLayersDetails(entity1, entity2);
        const compareResult2 = service.comparePageLayersDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePageLayersDetails(entity1, entity2);
        const compareResult2 = service.comparePageLayersDetails(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
