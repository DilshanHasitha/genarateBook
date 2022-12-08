import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBooksRelatedOptionDetails } from '../books-related-option-details.model';
import { BooksRelatedOptionDetailsService } from '../service/books-related-option-details.service';

@Injectable({ providedIn: 'root' })
export class BooksRelatedOptionDetailsRoutingResolveService implements Resolve<IBooksRelatedOptionDetails | null> {
  constructor(protected service: BooksRelatedOptionDetailsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBooksRelatedOptionDetails | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((booksRelatedOptionDetails: HttpResponse<IBooksRelatedOptionDetails>) => {
          if (booksRelatedOptionDetails.body) {
            return of(booksRelatedOptionDetails.body);
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
