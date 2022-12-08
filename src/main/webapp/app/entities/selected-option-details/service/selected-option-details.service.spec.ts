import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISelectedOptionDetails } from '../selected-option-details.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../selected-option-details.test-samples';

import { SelectedOptionDetailsService } from './selected-option-details.service';

const requireRestSample: ISelectedOptionDetails = {
  ...sampleWithRequiredData,
};

describe('SelectedOptionDetails Service', () => {
  let service: SelectedOptionDetailsService;
  let httpMock: HttpTestingController;
  let expectedResult: ISelectedOptionDetails | ISelectedOptionDetails[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SelectedOptionDetailsService);
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

    it('should create a SelectedOptionDetails', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const selectedOptionDetails = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(selectedOptionDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SelectedOptionDetails', () => {
      const selectedOptionDetails = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(selectedOptionDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SelectedOptionDetails', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SelectedOptionDetails', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SelectedOptionDetails', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSelectedOptionDetailsToCollectionIfMissing', () => {
      it('should add a SelectedOptionDetails to an empty array', () => {
        const selectedOptionDetails: ISelectedOptionDetails = sampleWithRequiredData;
        expectedResult = service.addSelectedOptionDetailsToCollectionIfMissing([], selectedOptionDetails);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(selectedOptionDetails);
      });

      it('should not add a SelectedOptionDetails to an array that contains it', () => {
        const selectedOptionDetails: ISelectedOptionDetails = sampleWithRequiredData;
        const selectedOptionDetailsCollection: ISelectedOptionDetails[] = [
          {
            ...selectedOptionDetails,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSelectedOptionDetailsToCollectionIfMissing(selectedOptionDetailsCollection, selectedOptionDetails);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SelectedOptionDetails to an array that doesn't contain it", () => {
        const selectedOptionDetails: ISelectedOptionDetails = sampleWithRequiredData;
        const selectedOptionDetailsCollection: ISelectedOptionDetails[] = [sampleWithPartialData];
        expectedResult = service.addSelectedOptionDetailsToCollectionIfMissing(selectedOptionDetailsCollection, selectedOptionDetails);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(selectedOptionDetails);
      });

      it('should add only unique SelectedOptionDetails to an array', () => {
        const selectedOptionDetailsArray: ISelectedOptionDetails[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const selectedOptionDetailsCollection: ISelectedOptionDetails[] = [sampleWithRequiredData];
        expectedResult = service.addSelectedOptionDetailsToCollectionIfMissing(
          selectedOptionDetailsCollection,
          ...selectedOptionDetailsArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const selectedOptionDetails: ISelectedOptionDetails = sampleWithRequiredData;
        const selectedOptionDetails2: ISelectedOptionDetails = sampleWithPartialData;
        expectedResult = service.addSelectedOptionDetailsToCollectionIfMissing([], selectedOptionDetails, selectedOptionDetails2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(selectedOptionDetails);
        expect(expectedResult).toContain(selectedOptionDetails2);
      });

      it('should accept null and undefined values', () => {
        const selectedOptionDetails: ISelectedOptionDetails = sampleWithRequiredData;
        expectedResult = service.addSelectedOptionDetailsToCollectionIfMissing([], null, selectedOptionDetails, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(selectedOptionDetails);
      });

      it('should return initial array if no SelectedOptionDetails is added', () => {
        const selectedOptionDetailsCollection: ISelectedOptionDetails[] = [sampleWithRequiredData];
        expectedResult = service.addSelectedOptionDetailsToCollectionIfMissing(selectedOptionDetailsCollection, undefined, null);
        expect(expectedResult).toEqual(selectedOptionDetailsCollection);
      });
    });

    describe('compareSelectedOptionDetails', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSelectedOptionDetails(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSelectedOptionDetails(entity1, entity2);
        const compareResult2 = service.compareSelectedOptionDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSelectedOptionDetails(entity1, entity2);
        const compareResult2 = service.compareSelectedOptionDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSelectedOptionDetails(entity1, entity2);
        const compareResult2 = service.compareSelectedOptionDetails(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
