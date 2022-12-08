import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBooksVariables } from '../books-variables.model';
import { BooksVariablesService } from '../service/books-variables.service';

@Injectable({ providedIn: 'root' })
export class BooksVariablesRoutingResolveService implements Resolve<IBooksVariables | null> {
  constructor(protected service: BooksVariablesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBooksVariables | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((booksVariables: HttpResponse<IBooksVariables>) => {
          if (booksVariables.body) {
            return of(booksVariables.body);
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
