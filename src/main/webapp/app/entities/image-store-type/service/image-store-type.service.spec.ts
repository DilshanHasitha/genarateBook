import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IImageStoreType } from '../image-store-type.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../image-store-type.test-samples';

import { ImageStoreTypeService } from './image-store-type.service';

const requireRestSample: IImageStoreType = {
  ...sampleWithRequiredData,
};

describe('ImageStoreType Service', () => {
  let service: ImageStoreTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IImageStoreType | IImageStoreType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ImageStoreTypeService);
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

    it('should create a ImageStoreType', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const imageStoreType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(imageStoreType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ImageStoreType', () => {
      const imageStoreType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(imageStoreType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ImageStoreType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ImageStoreType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ImageStoreType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addImageStoreTypeToCollectionIfMissing', () => {
      it('should add a ImageStoreType to an empty array', () => {
        const imageStoreType: IImageStoreType = sampleWithRequiredData;
        expectedResult = service.addImageStoreTypeToCollectionIfMissing([], imageStoreType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(imageStoreType);
      });

      it('should not add a ImageStoreType to an array that contains it', () => {
        const imageStoreType: IImageStoreType = sampleWithRequiredData;
        const imageStoreTypeCollection: IImageStoreType[] = [
          {
            ...imageStoreType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addImageStoreTypeToCollectionIfMissing(imageStoreTypeCollection, imageStoreType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ImageStoreType to an array that doesn't contain it", () => {
        const imageStoreType: IImageStoreType = sampleWithRequiredData;
        const imageStoreTypeCollection: IImageStoreType[] = [sampleWithPartialData];
        expectedResult = service.addImageStoreTypeToCollectionIfMissing(imageStoreTypeCollection, imageStoreType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(imageStoreType);
      });

      it('should add only unique ImageStoreType to an array', () => {
        const imageStoreTypeArray: IImageStoreType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const imageStoreTypeCollection: IImageStoreType[] = [sampleWithRequiredData];
        expectedResult = service.addImageStoreTypeToCollectionIfMissing(imageStoreTypeCollection, ...imageStoreTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const imageStoreType: IImageStoreType = sampleWithRequiredData;
        const imageStoreType2: IImageStoreType = sampleWithPartialData;
        expectedResult = service.addImageStoreTypeToCollectionIfMissing([], imageStoreType, imageStoreType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(imageStoreType);
        expect(expectedResult).toContain(imageStoreType2);
      });

      it('should accept null and undefined values', () => {
        const imageStoreType: IImageStoreType = sampleWithRequiredData;
        expectedResult = service.addImageStoreTypeToCollectionIfMissing([], null, imageStoreType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(imageStoreType);
      });

      it('should return initial array if no ImageStoreType is added', () => {
        const imageStoreTypeCollection: IImageStoreType[] = [sampleWithRequiredData];
        expectedResult = service.addImageStoreTypeToCollectionIfMissing(imageStoreTypeCollection, undefined, null);
        expect(expectedResult).toEqual(imageStoreTypeCollection);
      });
    });

    describe('compareImageStoreType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareImageStoreType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareImageStoreType(entity1, entity2);
        const compareResult2 = service.compareImageStoreType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareImageStoreType(entity1, entity2);
        const compareResult2 = service.compareImageStoreType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareImageStoreType(entity1, entity2);
        const compareResult2 = service.compareImageStoreType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
