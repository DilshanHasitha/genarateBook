import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPageLayersDetails } from '../page-layers-details.model';
import { PageLayersDetailsService } from '../service/page-layers-details.service';

@Injectable({ providedIn: 'root' })
export class PageLayersDetailsRoutingResolveService implements Resolve<IPageLayersDetails | null> {
  constructor(protected service: PageLayersDetailsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPageLayersDetails | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pageLayersDetails: HttpResponse<IPageLayersDetails>) => {
          if (pageLayersDetails.body) {
            return of(pageLayersDetails.body);
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
