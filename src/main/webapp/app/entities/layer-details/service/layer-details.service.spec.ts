import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILayerDetails } from '../layer-details.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../layer-details.test-samples';

import { LayerDetailsService } from './layer-details.service';

const requireRestSample: ILayerDetails = {
  ...sampleWithRequiredData,
};

describe('LayerDetails Service', () => {
  let service: LayerDetailsService;
  let httpMock: HttpTestingController;
  let expectedResult: ILayerDetails | ILayerDetails[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LayerDetailsService);
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

    it('should create a LayerDetails', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const layerDetails = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(layerDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LayerDetails', () => {
      const layerDetails = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(layerDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LayerDetails', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LayerDetails', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a LayerDetails', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLayerDetailsToCollectionIfMissing', () => {
      it('should add a LayerDetails to an empty array', () => {
        const layerDetails: ILayerDetails = sampleWithRequiredData;
        expectedResult = service.addLayerDetailsToCollectionIfMissing([], layerDetails);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(layerDetails);
      });

      it('should not add a LayerDetails to an array that contains it', () => {
        const layerDetails: ILayerDetails = sampleWithRequiredData;
        const layerDetailsCollection: ILayerDetails[] = [
          {
            ...layerDetails,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLayerDetailsToCollectionIfMissing(layerDetailsCollection, layerDetails);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LayerDetails to an array that doesn't contain it", () => {
        const layerDetails: ILayerDetails = sampleWithRequiredData;
        const layerDetailsCollection: ILayerDetails[] = [sampleWithPartialData];
        expectedResult = service.addLayerDetailsToCollectionIfMissing(layerDetailsCollection, layerDetails);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(layerDetails);
      });

      it('should add only unique LayerDetails to an array', () => {
        const layerDetailsArray: ILayerDetails[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const layerDetailsCollection: ILayerDetails[] = [sampleWithRequiredData];
        expectedResult = service.addLayerDetailsToCollectionIfMissing(layerDetailsCollection, ...layerDetailsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const layerDetails: ILayerDetails = sampleWithRequiredData;
        const layerDetails2: ILayerDetails = sampleWithPartialData;
        expectedResult = service.addLayerDetailsToCollectionIfMissing([], layerDetails, layerDetails2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(layerDetails);
        expect(expectedResult).toContain(layerDetails2);
      });

      it('should accept null and undefined values', () => {
        const layerDetails: ILayerDetails = sampleWithRequiredData;
        expectedResult = service.addLayerDetailsToCollectionIfMissing([], null, layerDetails, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(layerDetails);
      });

      it('should return initial array if no LayerDetails is added', () => {
        const layerDetailsCollection: ILayerDetails[] = [sampleWithRequiredData];
        expectedResult = service.addLayerDetailsToCollectionIfMissing(layerDetailsCollection, undefined, null);
        expect(expectedResult).toEqual(layerDetailsCollection);
      });
    });

    describe('compareLayerDetails', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLayerDetails(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareLayerDetails(entity1, entity2);
        const compareResult2 = service.compareLayerDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareLayerDetails(entity1, entity2);
        const compareResult2 = service.compareLayerDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareLayerDetails(entity1, entity2);
        const compareResult2 = service.compareLayerDetails(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
