import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPriceRelatedOptionDetails } from '../price-related-option-details.model';
import { PriceRelatedOptionDetailsService } from '../service/price-related-option-details.service';

@Injectable({ providedIn: 'root' })
export class PriceRelatedOptionDetailsRoutingResolveService implements Resolve<IPriceRelatedOptionDetails | null> {
  constructor(protected service: PriceRelatedOptionDetailsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPriceRelatedOptionDetails | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((priceRelatedOptionDetails: HttpResponse<IPriceRelatedOptionDetails>) => {
          if (priceRelatedOptionDetails.body) {
            return of(priceRelatedOptionDetails.body);
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
