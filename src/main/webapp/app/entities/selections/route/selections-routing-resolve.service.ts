import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISelections } from '../selections.model';
import { SelectionsService } from '../service/selections.service';

@Injectable({ providedIn: 'root' })
export class SelectionsRoutingResolveService implements Resolve<ISelections | null> {
  constructor(protected service: SelectionsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISelections | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((selections: HttpResponse<ISelections>) => {
          if (selections.body) {
            return of(selections.body);
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
