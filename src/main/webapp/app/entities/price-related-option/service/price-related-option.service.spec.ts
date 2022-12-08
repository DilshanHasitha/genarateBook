import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPriceRelatedOption } from '../price-related-option.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../price-related-option.test-samples';

import { PriceRelatedOptionService } from './price-related-option.service';

const requireRestSample: IPriceRelatedOption = {
  ...sampleWithRequiredData,
};

describe('PriceRelatedOption Service', () => {
  let service: PriceRelatedOptionService;
  let httpMock: HttpTestingController;
  let expectedResult: IPriceRelatedOption | IPriceRelatedOption[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PriceRelatedOptionService);
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

    it('should create a PriceRelatedOption', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const priceRelatedOption = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(priceRelatedOption).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PriceRelatedOption', () => {
      const priceRelatedOption = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(priceRelatedOption).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PriceRelatedOption', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PriceRelatedOption', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PriceRelatedOption', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPriceRelatedOptionToCollectionIfMissing', () => {
      it('should add a PriceRelatedOption to an empty array', () => {
        const priceRelatedOption: IPriceRelatedOption = sampleWithRequiredData;
        expectedResult = service.addPriceRelatedOptionToCollectionIfMissing([], priceRelatedOption);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(priceRelatedOption);
      });

      it('should not add a PriceRelatedOption to an array that contains it', () => {
        const priceRelatedOption: IPriceRelatedOption = sampleWithRequiredData;
        const priceRelatedOptionCollection: IPriceRelatedOption[] = [
          {
            ...priceRelatedOption,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPriceRelatedOptionToCollectionIfMissing(priceRelatedOptionCollection, priceRelatedOption);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PriceRelatedOption to an array that doesn't contain it", () => {
        const priceRelatedOption: IPriceRelatedOption = sampleWithRequiredData;
        const priceRelatedOptionCollection: IPriceRelatedOption[] = [sampleWithPartialData];
        expectedResult = service.addPriceRelatedOptionToCollectionIfMissing(priceRelatedOptionCollection, priceRelatedOption);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(priceRelatedOption);
      });

      it('should add only unique PriceRelatedOption to an array', () => {
        const priceRelatedOptionArray: IPriceRelatedOption[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const priceRelatedOptionCollection: IPriceRelatedOption[] = [sampleWithRequiredData];
        expectedResult = service.addPriceRelatedOptionToCollectionIfMissing(priceRelatedOptionCollection, ...priceRelatedOptionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const priceRelatedOption: IPriceRelatedOption = sampleWithRequiredData;
        const priceRelatedOption2: IPriceRelatedOption = sampleWithPartialData;
        expectedResult = service.addPriceRelatedOptionToCollectionIfMissing([], priceRelatedOption, priceRelatedOption2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(priceRelatedOption);
        expect(expectedResult).toContain(priceRelatedOption2);
      });

      it('should accept null and undefined values', () => {
        const priceRelatedOption: IPriceRelatedOption = sampleWithRequiredData;
        expectedResult = service.addPriceRelatedOptionToCollectionIfMissing([], null, priceRelatedOption, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(priceRelatedOption);
      });

      it('should return initial array if no PriceRelatedOption is added', () => {
        const priceRelatedOptionCollection: IPriceRelatedOption[] = [sampleWithRequiredData];
        expectedResult = service.addPriceRelatedOptionToCollectionIfMissing(priceRelatedOptionCollection, undefined, null);
        expect(expectedResult).toEqual(priceRelatedOptionCollection);
      });
    });

    describe('comparePriceRelatedOption', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePriceRelatedOption(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePriceRelatedOption(entity1, entity2);
        const compareResult2 = service.comparePriceRelatedOption(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePriceRelatedOption(entity1, entity2);
        const compareResult2 = service.comparePriceRelatedOption(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePriceRelatedOption(entity1, entity2);
        const compareResult2 = service.comparePriceRelatedOption(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
