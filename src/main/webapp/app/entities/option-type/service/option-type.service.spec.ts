import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOptionType } from '../option-type.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../option-type.test-samples';

import { OptionTypeService } from './option-type.service';

const requireRestSample: IOptionType = {
  ...sampleWithRequiredData,
};

describe('OptionType Service', () => {
  let service: OptionTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IOptionType | IOptionType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OptionTypeService);
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

    it('should create a OptionType', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const optionType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(optionType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OptionType', () => {
      const optionType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(optionType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OptionType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OptionType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OptionType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOptionTypeToCollectionIfMissing', () => {
      it('should add a OptionType to an empty array', () => {
        const optionType: IOptionType = sampleWithRequiredData;
        expectedResult = service.addOptionTypeToCollectionIfMissing([], optionType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(optionType);
      });

      it('should not add a OptionType to an array that contains it', () => {
        const optionType: IOptionType = sampleWithRequiredData;
        const optionTypeCollection: IOptionType[] = [
          {
            ...optionType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOptionTypeToCollectionIfMissing(optionTypeCollection, optionType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OptionType to an array that doesn't contain it", () => {
        const optionType: IOptionType = sampleWithRequiredData;
        const optionTypeCollection: IOptionType[] = [sampleWithPartialData];
        expectedResult = service.addOptionTypeToCollectionIfMissing(optionTypeCollection, optionType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(optionType);
      });

      it('should add only unique OptionType to an array', () => {
        const optionTypeArray: IOptionType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const optionTypeCollection: IOptionType[] = [sampleWithRequiredData];
        expectedResult = service.addOptionTypeToCollectionIfMissing(optionTypeCollection, ...optionTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const optionType: IOptionType = sampleWithRequiredData;
        const optionType2: IOptionType = sampleWithPartialData;
        expectedResult = service.addOptionTypeToCollectionIfMissing([], optionType, optionType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(optionType);
        expect(expectedResult).toContain(optionType2);
      });

      it('should accept null and undefined values', () => {
        const optionType: IOptionType = sampleWithRequiredData;
        expectedResult = service.addOptionTypeToCollectionIfMissing([], null, optionType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(optionType);
      });

      it('should return initial array if no OptionType is added', () => {
        const optionTypeCollection: IOptionType[] = [sampleWithRequiredData];
        expectedResult = service.addOptionTypeToCollectionIfMissing(optionTypeCollection, undefined, null);
        expect(expectedResult).toEqual(optionTypeCollection);
      });
    });

    describe('compareOptionType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOptionType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOptionType(entity1, entity2);
        const compareResult2 = service.compareOptionType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOptionType(entity1, entity2);
        const compareResult2 = service.compareOptionType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOptionType(entity1, entity2);
        const compareResult2 = service.compareOptionType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
