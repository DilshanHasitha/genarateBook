import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILayers } from '../layers.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../layers.test-samples';

import { LayersService } from './layers.service';

const requireRestSample: ILayers = {
  ...sampleWithRequiredData,
};

describe('Layers Service', () => {
  let service: LayersService;
  let httpMock: HttpTestingController;
  let expectedResult: ILayers | ILayers[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LayersService);
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

    it('should create a Layers', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const layers = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(layers).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Layers', () => {
      const layers = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(layers).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Layers', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Layers', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Layers', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLayersToCollectionIfMissing', () => {
      it('should add a Layers to an empty array', () => {
        const layers: ILayers = sampleWithRequiredData;
        expectedResult = service.addLayersToCollectionIfMissing([], layers);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(layers);
      });

      it('should not add a Layers to an array that contains it', () => {
        const layers: ILayers = sampleWithRequiredData;
        const layersCollection: ILayers[] = [
          {
            ...layers,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLayersToCollectionIfMissing(layersCollection, layers);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Layers to an array that doesn't contain it", () => {
        const layers: ILayers = sampleWithRequiredData;
        const layersCollection: ILayers[] = [sampleWithPartialData];
        expectedResult = service.addLayersToCollectionIfMissing(layersCollection, layers);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(layers);
      });

      it('should add only unique Layers to an array', () => {
        const layersArray: ILayers[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const layersCollection: ILayers[] = [sampleWithRequiredData];
        expectedResult = service.addLayersToCollectionIfMissing(layersCollection, ...layersArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const layers: ILayers = sampleWithRequiredData;
        const layers2: ILayers = sampleWithPartialData;
        expectedResult = service.addLayersToCollectionIfMissing([], layers, layers2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(layers);
        expect(expectedResult).toContain(layers2);
      });

      it('should accept null and undefined values', () => {
        const layers: ILayers = sampleWithRequiredData;
        expectedResult = service.addLayersToCollectionIfMissing([], null, layers, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(layers);
      });

      it('should return initial array if no Layers is added', () => {
        const layersCollection: ILayers[] = [sampleWithRequiredData];
        expectedResult = service.addLayersToCollectionIfMissing(layersCollection, undefined, null);
        expect(expectedResult).toEqual(layersCollection);
      });
    });

    describe('compareLayers', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLayers(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareLayers(entity1, entity2);
        const compareResult2 = service.compareLayers(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareLayers(entity1, entity2);
        const compareResult2 = service.compareLayers(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareLayers(entity1, entity2);
        const compareResult2 = service.compareLayers(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
