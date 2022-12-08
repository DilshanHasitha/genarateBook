import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAvatarAttributes } from '../avatar-attributes.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../avatar-attributes.test-samples';

import { AvatarAttributesService } from './avatar-attributes.service';

const requireRestSample: IAvatarAttributes = {
  ...sampleWithRequiredData,
};

describe('AvatarAttributes Service', () => {
  let service: AvatarAttributesService;
  let httpMock: HttpTestingController;
  let expectedResult: IAvatarAttributes | IAvatarAttributes[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AvatarAttributesService);
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

    it('should create a AvatarAttributes', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const avatarAttributes = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(avatarAttributes).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AvatarAttributes', () => {
      const avatarAttributes = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(avatarAttributes).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AvatarAttributes', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AvatarAttributes', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AvatarAttributes', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAvatarAttributesToCollectionIfMissing', () => {
      it('should add a AvatarAttributes to an empty array', () => {
        const avatarAttributes: IAvatarAttributes = sampleWithRequiredData;
        expectedResult = service.addAvatarAttributesToCollectionIfMissing([], avatarAttributes);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(avatarAttributes);
      });

      it('should not add a AvatarAttributes to an array that contains it', () => {
        const avatarAttributes: IAvatarAttributes = sampleWithRequiredData;
        const avatarAttributesCollection: IAvatarAttributes[] = [
          {
            ...avatarAttributes,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAvatarAttributesToCollectionIfMissing(avatarAttributesCollection, avatarAttributes);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AvatarAttributes to an array that doesn't contain it", () => {
        const avatarAttributes: IAvatarAttributes = sampleWithRequiredData;
        const avatarAttributesCollection: IAvatarAttributes[] = [sampleWithPartialData];
        expectedResult = service.addAvatarAttributesToCollectionIfMissing(avatarAttributesCollection, avatarAttributes);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(avatarAttributes);
      });

      it('should add only unique AvatarAttributes to an array', () => {
        const avatarAttributesArray: IAvatarAttributes[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const avatarAttributesCollection: IAvatarAttributes[] = [sampleWithRequiredData];
        expectedResult = service.addAvatarAttributesToCollectionIfMissing(avatarAttributesCollection, ...avatarAttributesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const avatarAttributes: IAvatarAttributes = sampleWithRequiredData;
        const avatarAttributes2: IAvatarAttributes = sampleWithPartialData;
        expectedResult = service.addAvatarAttributesToCollectionIfMissing([], avatarAttributes, avatarAttributes2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(avatarAttributes);
        expect(expectedResult).toContain(avatarAttributes2);
      });

      it('should accept null and undefined values', () => {
        const avatarAttributes: IAvatarAttributes = sampleWithRequiredData;
        expectedResult = service.addAvatarAttributesToCollectionIfMissing([], null, avatarAttributes, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(avatarAttributes);
      });

      it('should return initial array if no AvatarAttributes is added', () => {
        const avatarAttributesCollection: IAvatarAttributes[] = [sampleWithRequiredData];
        expectedResult = service.addAvatarAttributesToCollectionIfMissing(avatarAttributesCollection, undefined, null);
        expect(expectedResult).toEqual(avatarAttributesCollection);
      });
    });

    describe('compareAvatarAttributes', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAvatarAttributes(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAvatarAttributes(entity1, entity2);
        const compareResult2 = service.compareAvatarAttributes(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAvatarAttributes(entity1, entity2);
        const compareResult2 = service.compareAvatarAttributes(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAvatarAttributes(entity1, entity2);
        const compareResult2 = service.compareAvatarAttributes(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
