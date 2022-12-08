import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStyles } from '../styles.model';
import { StylesService } from '../service/styles.service';

@Injectable({ providedIn: 'root' })
export class StylesRoutingResolveService implements Resolve<IStyles | null> {
  constructor(protected service: StylesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStyles | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((styles: HttpResponse<IStyles>) => {
          if (styles.body) {
            return of(styles.body);
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
