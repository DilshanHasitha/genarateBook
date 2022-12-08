import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILayerGroup } from '../layer-group.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../layer-group.test-samples';

import { LayerGroupService } from './layer-group.service';

const requireRestSample: ILayerGroup = {
  ...sampleWithRequiredData,
};

describe('LayerGroup Service', () => {
  let service: LayerGroupService;
  let httpMock: HttpTestingController;
  let expectedResult: ILayerGroup | ILayerGroup[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LayerGroupService);
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

    it('should create a LayerGroup', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const layerGroup = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(layerGroup).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LayerGroup', () => {
      const layerGroup = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(layerGroup).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LayerGroup', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LayerGroup', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a LayerGroup', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLayerGroupToCollectionIfMissing', () => {
      it('should add a LayerGroup to an empty array', () => {
        const layerGroup: ILayerGroup = sampleWithRequiredData;
        expectedResult = service.addLayerGroupToCollectionIfMissing([], layerGroup);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(layerGroup);
      });

      it('should not add a LayerGroup to an array that contains it', () => {
        const layerGroup: ILayerGroup = sampleWithRequiredData;
        const layerGroupCollection: ILayerGroup[] = [
          {
            ...layerGroup,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLayerGroupToCollectionIfMissing(layerGroupCollection, layerGroup);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LayerGroup to an array that doesn't contain it", () => {
        const layerGroup: ILayerGroup = sampleWithRequiredData;
        const layerGroupCollection: ILayerGroup[] = [sampleWithPartialData];
        expectedResult = service.addLayerGroupToCollectionIfMissing(layerGroupCollection, layerGroup);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(layerGroup);
      });

      it('should add only unique LayerGroup to an array', () => {
        const layerGroupArray: ILayerGroup[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const layerGroupCollection: ILayerGroup[] = [sampleWithRequiredData];
        expectedResult = service.addLayerGroupToCollectionIfMissing(layerGroupCollection, ...layerGroupArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const layerGroup: ILayerGroup = sampleWithRequiredData;
        const layerGroup2: ILayerGroup = sampleWithPartialData;
        expectedResult = service.addLayerGroupToCollectionIfMissing([], layerGroup, layerGroup2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(layerGroup);
        expect(expectedResult).toContain(layerGroup2);
      });

      it('should accept null and undefined values', () => {
        const layerGroup: ILayerGroup = sampleWithRequiredData;
        expectedResult = service.addLayerGroupToCollectionIfMissing([], null, layerGroup, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(layerGroup);
      });

      it('should return initial array if no LayerGroup is added', () => {
        const layerGroupCollection: ILayerGroup[] = [sampleWithRequiredData];
        expectedResult = service.addLayerGroupToCollectionIfMissing(layerGroupCollection, undefined, null);
        expect(expectedResult).toEqual(layerGroupCollection);
      });
    });

    describe('compareLayerGroup', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLayerGroup(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareLayerGroup(entity1, entity2);
        const compareResult2 = service.compareLayerGroup(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareLayerGroup(entity1, entity2);
        const compareResult2 = service.compareLayerGroup(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareLayerGroup(entity1, entity2);
        const compareResult2 = service.compareLayerGroup(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
