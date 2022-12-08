import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPageSize } from '../page-size.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../page-size.test-samples';

import { PageSizeService } from './page-size.service';

const requireRestSample: IPageSize = {
  ...sampleWithRequiredData,
};

describe('PageSize Service', () => {
  let service: PageSizeService;
  let httpMock: HttpTestingController;
  let expectedResult: IPageSize | IPageSize[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PageSizeService);
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

    it('should create a PageSize', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const pageSize = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pageSize).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PageSize', () => {
      const pageSize = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pageSize).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PageSize', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PageSize', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PageSize', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPageSizeToCollectionIfMissing', () => {
      it('should add a PageSize to an empty array', () => {
        const pageSize: IPageSize = sampleWithRequiredData;
        expectedResult = service.addPageSizeToCollectionIfMissing([], pageSize);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pageSize);
      });

      it('should not add a PageSize to an array that contains it', () => {
        const pageSize: IPageSize = sampleWithRequiredData;
        const pageSizeCollection: IPageSize[] = [
          {
            ...pageSize,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPageSizeToCollectionIfMissing(pageSizeCollection, pageSize);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PageSize to an array that doesn't contain it", () => {
        const pageSize: IPageSize = sampleWithRequiredData;
        const pageSizeCollection: IPageSize[] = [sampleWithPartialData];
        expectedResult = service.addPageSizeToCollectionIfMissing(pageSizeCollection, pageSize);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pageSize);
      });

      it('should add only unique PageSize to an array', () => {
        const pageSizeArray: IPageSize[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pageSizeCollection: IPageSize[] = [sampleWithRequiredData];
        expectedResult = service.addPageSizeToCollectionIfMissing(pageSizeCollection, ...pageSizeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pageSize: IPageSize = sampleWithRequiredData;
        const pageSize2: IPageSize = sampleWithPartialData;
        expectedResult = service.addPageSizeToCollectionIfMissing([], pageSize, pageSize2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pageSize);
        expect(expectedResult).toContain(pageSize2);
      });

      it('should accept null and undefined values', () => {
        const pageSize: IPageSize = sampleWithRequiredData;
        expectedResult = service.addPageSizeToCollectionIfMissing([], null, pageSize, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pageSize);
      });

      it('should return initial array if no PageSize is added', () => {
        const pageSizeCollection: IPageSize[] = [sampleWithRequiredData];
        expectedResult = service.addPageSizeToCollectionIfMissing(pageSizeCollection, undefined, null);
        expect(expectedResult).toEqual(pageSizeCollection);
      });
    });

    describe('comparePageSize', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePageSize(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePageSize(entity1, entity2);
        const compareResult2 = service.comparePageSize(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePageSize(entity1, entity2);
        const compareResult2 = service.comparePageSize(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePageSize(entity1, entity2);
        const compareResult2 = service.comparePageSize(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
