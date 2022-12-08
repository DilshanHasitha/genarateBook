import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOptionType } from '../option-type.model';
import { OptionTypeService } from '../service/option-type.service';

@Injectable({ providedIn: 'root' })
export class OptionTypeRoutingResolveService implements Resolve<IOptionType | null> {
  constructor(protected service: OptionTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOptionType | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((optionType: HttpResponse<IOptionType>) => {
          if (optionType.body) {
            return of(optionType.body);
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
