import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBooksRelatedOption } from '../books-related-option.model';
import { BooksRelatedOptionService } from '../service/books-related-option.service';

@Injectable({ providedIn: 'root' })
export class BooksRelatedOptionRoutingResolveService implements Resolve<IBooksRelatedOption | null> {
  constructor(protected service: BooksRelatedOptionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBooksRelatedOption | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((booksRelatedOption: HttpResponse<IBooksRelatedOption>) => {
          if (booksRelatedOption.body) {
            return of(booksRelatedOption.body);
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
