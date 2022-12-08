import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBooksAttributes } from '../books-attributes.model';
import { BooksAttributesService } from '../service/books-attributes.service';

@Injectable({ providedIn: 'root' })
export class BooksAttributesRoutingResolveService implements Resolve<IBooksAttributes | null> {
  constructor(protected service: BooksAttributesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBooksAttributes | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((booksAttributes: HttpResponse<IBooksAttributes>) => {
          if (booksAttributes.body) {
            return of(booksAttributes.body);
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
