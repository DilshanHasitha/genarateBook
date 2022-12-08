import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPageLayers } from '../page-layers.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../page-layers.test-samples';

import { PageLayersService } from './page-layers.service';

const requireRestSample: IPageLayers = {
  ...sampleWithRequiredData,
};

describe('PageLayers Service', () => {
  let service: PageLayersService;
  let httpMock: HttpTestingController;
  let expectedResult: IPageLayers | IPageLayers[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PageLayersService);
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

    it('should create a PageLayers', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const pageLayers = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pageLayers).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PageLayers', () => {
      const pageLayers = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pageLayers).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PageLayers', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PageLayers', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PageLayers', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPageLayersToCollectionIfMissing', () => {
      it('should add a PageLayers to an empty array', () => {
        const pageLayers: IPageLayers = sampleWithRequiredData;
        expectedResult = service.addPageLayersToCollectionIfMissing([], pageLayers);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pageLayers);
      });

      it('should not add a PageLayers to an array that contains it', () => {
        const pageLayers: IPageLayers = sampleWithRequiredData;
        const pageLayersCollection: IPageLayers[] = [
          {
            ...pageLayers,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPageLayersToCollectionIfMissing(pageLayersCollection, pageLayers);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PageLayers to an array that doesn't contain it", () => {
        const pageLayers: IPageLayers = sampleWithRequiredData;
        const pageLayersCollection: IPageLayers[] = [sampleWithPartialData];
        expectedResult = service.addPageLayersToCollectionIfMissing(pageLayersCollection, pageLayers);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pageLayers);
      });

      it('should add only unique PageLayers to an array', () => {
        const pageLayersArray: IPageLayers[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pageLayersCollection: IPageLayers[] = [sampleWithRequiredData];
        expectedResult = service.addPageLayersToCollectionIfMissing(pageLayersCollection, ...pageLayersArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pageLayers: IPageLayers = sampleWithRequiredData;
        const pageLayers2: IPageLayers = sampleWithPartialData;
        expectedResult = service.addPageLayersToCollectionIfMissing([], pageLayers, pageLayers2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pageLayers);
        expect(expectedResult).toContain(pageLayers2);
      });

      it('should accept null and undefined values', () => {
        const pageLayers: IPageLayers = sampleWithRequiredData;
        expectedResult = service.addPageLayersToCollectionIfMissing([], null, pageLayers, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pageLayers);
      });

      it('should return initial array if no PageLayers is added', () => {
        const pageLayersCollection: IPageLayers[] = [sampleWithRequiredData];
        expectedResult = service.addPageLayersToCollectionIfMissing(pageLayersCollection, undefined, null);
        expect(expectedResult).toEqual(pageLayersCollection);
      });
    });

    describe('comparePageLayers', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePageLayers(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePageLayers(entity1, entity2);
        const compareResult2 = service.comparePageLayers(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePageLayers(entity1, entity2);
        const compareResult2 = service.comparePageLayers(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePageLayers(entity1, entity2);
        const compareResult2 = service.comparePageLayers(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
