import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStylesDetails } from '../styles-details.model';
import { StylesDetailsService } from '../service/styles-details.service';

@Injectable({ providedIn: 'root' })
export class StylesDetailsRoutingResolveService implements Resolve<IStylesDetails | null> {
  constructor(protected service: StylesDetailsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStylesDetails | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((stylesDetails: HttpResponse<IStylesDetails>) => {
          if (stylesDetails.body) {
            return of(stylesDetails.body);
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
