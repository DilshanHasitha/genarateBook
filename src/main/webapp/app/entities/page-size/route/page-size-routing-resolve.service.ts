import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPageSize } from '../page-size.model';
import { PageSizeService } from '../service/page-size.service';

@Injectable({ providedIn: 'root' })
export class PageSizeRoutingResolveService implements Resolve<IPageSize | null> {
  constructor(protected service: PageSizeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPageSize | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pageSize: HttpResponse<IPageSize>) => {
          if (pageSize.body) {
            return of(pageSize.body);
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
