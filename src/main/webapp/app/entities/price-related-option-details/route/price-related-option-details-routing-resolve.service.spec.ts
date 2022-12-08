import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IPriceRelatedOptionDetails } from '../price-related-option-details.model';
import { PriceRelatedOptionDetailsService } from '../service/price-related-option-details.service';

import { PriceRelatedOptionDetailsRoutingResolveService } from './price-related-option-details-routing-resolve.service';

describe('PriceRelatedOptionDetails routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PriceRelatedOptionDetailsRoutingResolveService;
  let service: PriceRelatedOptionDetailsService;
  let resultPriceRelatedOptionDetails: IPriceRelatedOptionDetails | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(PriceRelatedOptionDetailsRoutingResolveService);
    service = TestBed.inject(PriceRelatedOptionDetailsService);
    resultPriceRelatedOptionDetails = undefined;
  });

  describe('resolve', () => {
    it('should return IPriceRelatedOptionDetails returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPriceRelatedOptionDetails = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPriceRelatedOptionDetails).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPriceRelatedOptionDetails = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPriceRelatedOptionDetails).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IPriceRelatedOptionDetails>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPriceRelatedOptionDetails = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPriceRelatedOptionDetails).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
