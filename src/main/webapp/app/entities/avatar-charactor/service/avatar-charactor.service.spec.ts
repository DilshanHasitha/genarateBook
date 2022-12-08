import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAvatarCharactor } from '../avatar-charactor.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../avatar-charactor.test-samples';

import { AvatarCharactorService } from './avatar-charactor.service';

const requireRestSample: IAvatarCharactor = {
  ...sampleWithRequiredData,
};

describe('AvatarCharactor Service', () => {
  let service: AvatarCharactorService;
  let httpMock: HttpTestingController;
  let expectedResult: IAvatarCharactor | IAvatarCharactor[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AvatarCharactorService);
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

    it('should create a AvatarCharactor', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const avatarCharactor = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(avatarCharactor).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AvatarCharactor', () => {
      const avatarCharactor = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(avatarCharactor).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AvatarCharactor', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AvatarCharactor', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AvatarCharactor', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAvatarCharactorToCollectionIfMissing', () => {
      it('should add a AvatarCharactor to an empty array', () => {
        const avatarCharactor: IAvatarCharactor = sampleWithRequiredData;
        expectedResult = service.addAvatarCharactorToCollectionIfMissing([], avatarCharactor);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(avatarCharactor);
      });

      it('should not add a AvatarCharactor to an array that contains it', () => {
        const avatarCharactor: IAvatarCharactor = sampleWithRequiredData;
        const avatarCharactorCollection: IAvatarCharactor[] = [
          {
            ...avatarCharactor,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAvatarCharactorToCollectionIfMissing(avatarCharactorCollection, avatarCharactor);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AvatarCharactor to an array that doesn't contain it", () => {
        const avatarCharactor: IAvatarCharactor = sampleWithRequiredData;
        const avatarCharactorCollection: IAvatarCharactor[] = [sampleWithPartialData];
        expectedResult = service.addAvatarCharactorToCollectionIfMissing(avatarCharactorCollection, avatarCharactor);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(avatarCharactor);
      });

      it('should add only unique AvatarCharactor to an array', () => {
        const avatarCharactorArray: IAvatarCharactor[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const avatarCharactorCollection: IAvatarCharactor[] = [sampleWithRequiredData];
        expectedResult = service.addAvatarCharactorToCollectionIfMissing(avatarCharactorCollection, ...avatarCharactorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const avatarCharactor: IAvatarCharactor = sampleWithRequiredData;
        const avatarCharactor2: IAvatarCharactor = sampleWithPartialData;
        expectedResult = service.addAvatarCharactorToCollectionIfMissing([], avatarCharactor, avatarCharactor2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(avatarCharactor);
        expect(expectedResult).toContain(avatarCharactor2);
      });

      it('should accept null and undefined values', () => {
        const avatarCharactor: IAvatarCharactor = sampleWithRequiredData;
        expectedResult = service.addAvatarCharactorToCollectionIfMissing([], null, avatarCharactor, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(avatarCharactor);
      });

      it('should return initial array if no AvatarCharactor is added', () => {
        const avatarCharactorCollection: IAvatarCharactor[] = [sampleWithRequiredData];
        expectedResult = service.addAvatarCharactorToCollectionIfMissing(avatarCharactorCollection, undefined, null);
        expect(expectedResult).toEqual(avatarCharactorCollection);
      });
    });

    describe('compareAvatarCharactor', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAvatarCharactor(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAvatarCharactor(entity1, entity2);
        const compareResult2 = service.compareAvatarCharactor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAvatarCharactor(entity1, entity2);
        const compareResult2 = service.compareAvatarCharactor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAvatarCharactor(entity1, entity2);
        const compareResult2 = service.compareAvatarCharactor(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
