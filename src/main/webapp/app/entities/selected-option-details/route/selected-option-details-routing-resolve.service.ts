import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISelectedOptionDetails } from '../selected-option-details.model';
import { SelectedOptionDetailsService } from '../service/selected-option-details.service';

@Injectable({ providedIn: 'root' })
export class SelectedOptionDetailsRoutingResolveService implements Resolve<ISelectedOptionDetails | null> {
  constructor(protected service: SelectedOptionDetailsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISelectedOptionDetails | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((selectedOptionDetails: HttpResponse<ISelectedOptionDetails>) => {
          if (selectedOptionDetails.body) {
            return of(selectedOptionDetails.body);
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
