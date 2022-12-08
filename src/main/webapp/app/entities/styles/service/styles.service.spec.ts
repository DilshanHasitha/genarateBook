import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IStyles } from '../styles.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../styles.test-samples';

import { StylesService } from './styles.service';

const requireRestSample: IStyles = {
  ...sampleWithRequiredData,
};

describe('Styles Service', () => {
  let service: StylesService;
  let httpMock: HttpTestingController;
  let expectedResult: IStyles | IStyles[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(StylesService);
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

    it('should create a Styles', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const styles = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(styles).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Styles', () => {
      const styles = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(styles).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Styles', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Styles', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Styles', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addStylesToCollectionIfMissing', () => {
      it('should add a Styles to an empty array', () => {
        const styles: IStyles = sampleWithRequiredData;
        expectedResult = service.addStylesToCollectionIfMissing([], styles);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(styles);
      });

      it('should not add a Styles to an array that contains it', () => {
        const styles: IStyles = sampleWithRequiredData;
        const stylesCollection: IStyles[] = [
          {
            ...styles,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addStylesToCollectionIfMissing(stylesCollection, styles);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Styles to an array that doesn't contain it", () => {
        const styles: IStyles = sampleWithRequiredData;
        const stylesCollection: IStyles[] = [sampleWithPartialData];
        expectedResult = service.addStylesToCollectionIfMissing(stylesCollection, styles);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(styles);
      });

      it('should add only unique Styles to an array', () => {
        const stylesArray: IStyles[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const stylesCollection: IStyles[] = [sampleWithRequiredData];
        expectedResult = service.addStylesToCollectionIfMissing(stylesCollection, ...stylesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const styles: IStyles = sampleWithRequiredData;
        const styles2: IStyles = sampleWithPartialData;
        expectedResult = service.addStylesToCollectionIfMissing([], styles, styles2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(styles);
        expect(expectedResult).toContain(styles2);
      });

      it('should accept null and undefined values', () => {
        const styles: IStyles = sampleWithRequiredData;
        expectedResult = service.addStylesToCollectionIfMissing([], null, styles, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(styles);
      });

      it('should return initial array if no Styles is added', () => {
        const stylesCollection: IStyles[] = [sampleWithRequiredData];
        expectedResult = service.addStylesToCollectionIfMissing(stylesCollection, undefined, null);
        expect(expectedResult).toEqual(stylesCollection);
      });
    });

    describe('compareStyles', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareStyles(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareStyles(entity1, entity2);
        const compareResult2 = service.compareStyles(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareStyles(entity1, entity2);
        const compareResult2 = service.compareStyles(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareStyles(entity1, entity2);
        const compareResult2 = service.compareStyles(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
