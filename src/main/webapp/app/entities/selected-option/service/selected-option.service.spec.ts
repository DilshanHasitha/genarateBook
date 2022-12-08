import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISelectedOption } from '../selected-option.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../selected-option.test-samples';

import { SelectedOptionService, RestSelectedOption } from './selected-option.service';

const requireRestSample: RestSelectedOption = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.format(DATE_FORMAT),
};

describe('SelectedOption Service', () => {
  let service: SelectedOptionService;
  let httpMock: HttpTestingController;
  let expectedResult: ISelectedOption | ISelectedOption[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SelectedOptionService);
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

    it('should create a SelectedOption', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const selectedOption = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(selectedOption).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SelectedOption', () => {
      const selectedOption = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(selectedOption).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SelectedOption', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SelectedOption', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SelectedOption', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSelectedOptionToCollectionIfMissing', () => {
      it('should add a SelectedOption to an empty array', () => {
        const selectedOption: ISelectedOption = sampleWithRequiredData;
        expectedResult = service.addSelectedOptionToCollectionIfMissing([], selectedOption);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(selectedOption);
      });

      it('should not add a SelectedOption to an array that contains it', () => {
        const selectedOption: ISelectedOption = sampleWithRequiredData;
        const selectedOptionCollection: ISelectedOption[] = [
          {
            ...selectedOption,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSelectedOptionToCollectionIfMissing(selectedOptionCollection, selectedOption);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SelectedOption to an array that doesn't contain it", () => {
        const selectedOption: ISelectedOption = sampleWithRequiredData;
        const selectedOptionCollection: ISelectedOption[] = [sampleWithPartialData];
        expectedResult = service.addSelectedOptionToCollectionIfMissing(selectedOptionCollection, selectedOption);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(selectedOption);
      });

      it('should add only unique SelectedOption to an array', () => {
        const selectedOptionArray: ISelectedOption[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const selectedOptionCollection: ISelectedOption[] = [sampleWithRequiredData];
        expectedResult = service.addSelectedOptionToCollectionIfMissing(selectedOptionCollection, ...selectedOptionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const selectedOption: ISelectedOption = sampleWithRequiredData;
        const selectedOption2: ISelectedOption = sampleWithPartialData;
        expectedResult = service.addSelectedOptionToCollectionIfMissing([], selectedOption, selectedOption2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(selectedOption);
        expect(expectedResult).toContain(selectedOption2);
      });

      it('should accept null and undefined values', () => {
        const selectedOption: ISelectedOption = sampleWithRequiredData;
        expectedResult = service.addSelectedOptionToCollectionIfMissing([], null, selectedOption, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(selectedOption);
      });

      it('should return initial array if no SelectedOption is added', () => {
        const selectedOptionCollection: ISelectedOption[] = [sampleWithRequiredData];
        expectedResult = service.addSelectedOptionToCollectionIfMissing(selectedOptionCollection, undefined, null);
        expect(expectedResult).toEqual(selectedOptionCollection);
      });
    });

    describe('compareSelectedOption', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSelectedOption(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSelectedOption(entity1, entity2);
        const compareResult2 = service.compareSelectedOption(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSelectedOption(entity1, entity2);
        const compareResult2 = service.compareSelectedOption(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSelectedOption(entity1, entity2);
        const compareResult2 = service.compareSelectedOption(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
