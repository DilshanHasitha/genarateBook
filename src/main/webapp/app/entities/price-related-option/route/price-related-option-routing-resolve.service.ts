import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPriceRelatedOption } from '../price-related-option.model';
import { PriceRelatedOptionService } from '../service/price-related-option.service';

@Injectable({ providedIn: 'root' })
export class PriceRelatedOptionRoutingResolveService implements Resolve<IPriceRelatedOption | null> {
  constructor(protected service: PriceRelatedOptionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPriceRelatedOption | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((priceRelatedOption: HttpResponse<IPriceRelatedOption>) => {
          if (priceRelatedOption.body) {
            return of(priceRelatedOption.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
