import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISelectedOption } from '../selected-option.model';
import { SelectedOptionService } from '../service/selected-option.service';

@Injectable({ providedIn: 'root' })
export class SelectedOptionRoutingResolveService implements Resolve<ISelectedOption | null> {
  constructor(protected service: SelectedOptionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISelectedOption | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((selectedOption: HttpResponse<ISelectedOption>) => {
          if (selectedOption.body) {
            return of(selectedOption.body);
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
