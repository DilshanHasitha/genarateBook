import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBooksOptionDetails } from '../books-option-details.model';
import { BooksOptionDetailsService } from '../service/books-option-details.service';

@Injectable({ providedIn: 'root' })
export class BooksOptionDetailsRoutingResolveService implements Resolve<IBooksOptionDetails | null> {
  constructor(protected service: BooksOptionDetailsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBooksOptionDetails | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((booksOptionDetails: HttpResponse<IBooksOptionDetails>) => {
          if (booksOptionDetails.body) {
            return of(booksOptionDetails.body);
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
