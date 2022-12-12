import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IStylesDetails } from '../styles-details.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../styles-details.test-samples';

import { StylesDetailsService } from './styles-details.service';

const requireRestSample: IStylesDetails = {
  ...sampleWithRequiredData,
};

describe('StylesDetails Service', () => {
  let service: StylesDetailsService;
  let httpMock: HttpTestingController;
  let expectedResult: IStylesDetails | IStylesDetails[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(StylesDetailsService);
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

    it('should create a StylesDetails', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const stylesDetails = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(stylesDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a StylesDetails', () => {
      const stylesDetails = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(stylesDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a StylesDetails', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of StylesDetails', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a StylesDetails', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addStylesDetailsToCollectionIfMissing', () => {
      it('should add a StylesDetails to an empty array', () => {
        const stylesDetails: IStylesDetails = sampleWithRequiredData;
        expectedResult = service.addStylesDetailsToCollectionIfMissing([], stylesDetails);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stylesDetails);
      });

      it('should not add a StylesDetails to an array that contains it', () => {
        const stylesDetails: IStylesDetails = sampleWithRequiredData;
        const stylesDetailsCollection: IStylesDetails[] = [
          {
            ...stylesDetails,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addStylesDetailsToCollectionIfMissing(stylesDetailsCollection, stylesDetails);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StylesDetails to an array that doesn't contain it", () => {
        const stylesDetails: IStylesDetails = sampleWithRequiredData;
        const stylesDetailsCollection: IStylesDetails[] = [sampleWithPartialData];
        expectedResult = service.addStylesDetailsToCollectionIfMissing(stylesDetailsCollection, stylesDetails);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stylesDetails);
      });

      it('should add only unique StylesDetails to an array', () => {
        const stylesDetailsArray: IStylesDetails[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const stylesDetailsCollection: IStylesDetails[] = [sampleWithRequiredData];
        expectedResult = service.addStylesDetailsToCollectionIfMissing(stylesDetailsCollection, ...stylesDetailsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const stylesDetails: IStylesDetails = sampleWithRequiredData;
        const stylesDetails2: IStylesDetails = sampleWithPartialData;
        expectedResult = service.addStylesDetailsToCollectionIfMissing([], stylesDetails, stylesDetails2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stylesDetails);
        expect(expectedResult).toContain(stylesDetails2);
      });

      it('should accept null and undefined values', () => {
        const stylesDetails: IStylesDetails = sampleWithRequiredData;
        expectedResult = service.addStylesDetailsToCollectionIfMissing([], null, stylesDetails, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stylesDetails);
      });

      it('should return initial array if no StylesDetails is added', () => {
        const stylesDetailsCollection: IStylesDetails[] = [sampleWithRequiredData];
        expectedResult = service.addStylesDetailsToCollectionIfMissing(stylesDetailsCollection, undefined, null);
        expect(expectedResult).toEqual(stylesDetailsCollection);
      });
    });

    describe('compareStylesDetails', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareStylesDetails(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareStylesDetails(entity1, entity2);
        const compareResult2 = service.compareStylesDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareStylesDetails(entity1, entity2);
        const compareResult2 = service.compareStylesDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareStylesDetails(entity1, entity2);
        const compareResult2 = service.compareStylesDetails(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
