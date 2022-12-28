import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFontFamily } from '../font-family.model';
import { FontFamilyService } from '../service/font-family.service';

@Injectable({ providedIn: 'root' })
export class FontFamilyRoutingResolveService implements Resolve<IFontFamily | null> {
  constructor(protected service: FontFamilyService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFontFamily | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fontFamily: HttpResponse<IFontFamily>) => {
          if (fontFamily.body) {
            return of(fontFamily.body);
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
