import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrgProperty } from '../org-property.model';
import { OrgPropertyService } from '../service/org-property.service';

@Injectable({ providedIn: 'root' })
export class OrgPropertyRoutingResolveService implements Resolve<IOrgProperty | null> {
  constructor(protected service: OrgPropertyService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrgProperty | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((orgProperty: HttpResponse<IOrgProperty>) => {
          if (orgProperty.body) {
            return of(orgProperty.body);
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
