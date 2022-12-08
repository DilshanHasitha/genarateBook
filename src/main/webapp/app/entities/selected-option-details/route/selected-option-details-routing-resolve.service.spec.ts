import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ISelectedOptionDetails } from '../selected-option-details.model';
import { SelectedOptionDetailsService } from '../service/selected-option-details.service';

import { SelectedOptionDetailsRoutingResolveService } from './selected-option-details-routing-resolve.service';

describe('SelectedOptionDetails routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SelectedOptionDetailsRoutingResolveService;
  let service: SelectedOptionDetailsService;
  let resultSelectedOptionDetails: ISelectedOptionDetails | null | undefined;

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
    routingResolveService = TestBed.inject(SelectedOptionDetailsRoutingResolveService);
    service = TestBed.inject(SelectedOptionDetailsService);
    resultSelectedOptionDetails = undefined;
  });

  describe('resolve', () => {
    it('should return ISelectedOptionDetails returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSelectedOptionDetails = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSelectedOptionDetails).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSelectedOptionDetails = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSelectedOptionDetails).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ISelectedOptionDetails>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSelectedOptionDetails = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSelectedOptionDetails).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
