import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOptions } from '../options.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../options.test-samples';

import { OptionsService } from './options.service';

const requireRestSample: IOptions = {
  ...sampleWithRequiredData,
};

describe('Options Service', () => {
  let service: OptionsService;
  let httpMock: HttpTestingController;
  let expectedResult: IOptions | IOptions[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OptionsService);
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

    it('should create a Options', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const options = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(options).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Options', () => {
      const options = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(options).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Options', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Options', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Options', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOptionsToCollectionIfMissing', () => {
      it('should add a Options to an empty array', () => {
        const options: IOptions = sampleWithRequiredData;
        expectedResult = service.addOptionsToCollectionIfMissing([], options);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(options);
      });

      it('should not add a Options to an array that contains it', () => {
        const options: IOptions = sampleWithRequiredData;
        const optionsCollection: IOptions[] = [
          {
            ...options,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOptionsToCollectionIfMissing(optionsCollection, options);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Options to an array that doesn't contain it", () => {
        const options: IOptions = sampleWithRequiredData;
        const optionsCollection: IOptions[] = [sampleWithPartialData];
        expectedResult = service.addOptionsToCollectionIfMissing(optionsCollection, options);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(options);
      });

      it('should add only unique Options to an array', () => {
        const optionsArray: IOptions[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const optionsCollection: IOptions[] = [sampleWithRequiredData];
        expectedResult = service.addOptionsToCollectionIfMissing(optionsCollection, ...optionsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const options: IOptions = sampleWithRequiredData;
        const options2: IOptions = sampleWithPartialData;
        expectedResult = service.addOptionsToCollectionIfMissing([], options, options2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(options);
        expect(expectedResult).toContain(options2);
      });

      it('should accept null and undefined values', () => {
        const options: IOptions = sampleWithRequiredData;
        expectedResult = service.addOptionsToCollectionIfMissing([], null, options, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(options);
      });

      it('should return initial array if no Options is added', () => {
        const optionsCollection: IOptions[] = [sampleWithRequiredData];
        expectedResult = service.addOptionsToCollectionIfMissing(optionsCollection, undefined, null);
        expect(expectedResult).toEqual(optionsCollection);
      });
    });

    describe('compareOptions', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOptions(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOptions(entity1, entity2);
        const compareResult2 = service.compareOptions(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOptions(entity1, entity2);
        const compareResult2 = service.compareOptions(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOptions(entity1, entity2);
        const compareResult2 = service.compareOptions(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
