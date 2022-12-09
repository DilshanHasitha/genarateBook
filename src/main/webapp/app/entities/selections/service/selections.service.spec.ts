import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISelections } from '../selections.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../selections.test-samples';

import { SelectionsService } from './selections.service';

const requireRestSample: ISelections = {
  ...sampleWithRequiredData,
};

describe('Selections Service', () => {
  let service: SelectionsService;
  let httpMock: HttpTestingController;
  let expectedResult: ISelections | ISelections[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SelectionsService);
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

    it('should create a Selections', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const selections = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(selections).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Selections', () => {
      const selections = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(selections).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Selections', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Selections', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Selections', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSelectionsToCollectionIfMissing', () => {
      it('should add a Selections to an empty array', () => {
        const selections: ISelections = sampleWithRequiredData;
        expectedResult = service.addSelectionsToCollectionIfMissing([], selections);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(selections);
      });

      it('should not add a Selections to an array that contains it', () => {
        const selections: ISelections = sampleWithRequiredData;
        const selectionsCollection: ISelections[] = [
          {
            ...selections,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSelectionsToCollectionIfMissing(selectionsCollection, selections);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Selections to an array that doesn't contain it", () => {
        const selections: ISelections = sampleWithRequiredData;
        const selectionsCollection: ISelections[] = [sampleWithPartialData];
        expectedResult = service.addSelectionsToCollectionIfMissing(selectionsCollection, selections);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(selections);
      });

      it('should add only unique Selections to an array', () => {
        const selectionsArray: ISelections[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const selectionsCollection: ISelections[] = [sampleWithRequiredData];
        expectedResult = service.addSelectionsToCollectionIfMissing(selectionsCollection, ...selectionsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const selections: ISelections = sampleWithRequiredData;
        const selections2: ISelections = sampleWithPartialData;
        expectedResult = service.addSelectionsToCollectionIfMissing([], selections, selections2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(selections);
        expect(expectedResult).toContain(selections2);
      });

      it('should accept null and undefined values', () => {
        const selections: ISelections = sampleWithRequiredData;
        expectedResult = service.addSelectionsToCollectionIfMissing([], null, selections, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(selections);
      });

      it('should return initial array if no Selections is added', () => {
        const selectionsCollection: ISelections[] = [sampleWithRequiredData];
        expectedResult = service.addSelectionsToCollectionIfMissing(selectionsCollection, undefined, null);
        expect(expectedResult).toEqual(selectionsCollection);
      });
    });

    describe('compareSelections', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSelections(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSelections(entity1, entity2);
        const compareResult2 = service.compareSelections(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSelections(entity1, entity2);
        const compareResult2 = service.compareSelections(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSelections(entity1, entity2);
        const compareResult2 = service.compareSelections(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
