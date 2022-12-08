import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IBooksRelatedOption } from '../books-related-option.model';
import { BooksRelatedOptionService } from '../service/books-related-option.service';

import { BooksRelatedOptionRoutingResolveService } from './books-related-option-routing-resolve.service';

describe('BooksRelatedOption routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: BooksRelatedOptionRoutingResolveService;
  let service: BooksRelatedOptionService;
  let resultBooksRelatedOption: IBooksRelatedOption | null | undefined;

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
    routingResolveService = TestBed.inject(BooksRelatedOptionRoutingResolveService);
    service = TestBed.inject(BooksRelatedOptionService);
    resultBooksRelatedOption = undefined;
  });

  describe('resolve', () => {
    it('should return IBooksRelatedOption returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBooksRelatedOption = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBooksRelatedOption).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBooksRelatedOption = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultBooksRelatedOption).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IBooksRelatedOption>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBooksRelatedOption = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBooksRelatedOption).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
