import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBooksPage } from '../books-page.model';
import { BooksPageService } from '../service/books-page.service';

@Injectable({ providedIn: 'root' })
export class BooksPageRoutingResolveService implements Resolve<IBooksPage | null> {
  constructor(protected service: BooksPageService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBooksPage | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((booksPage: HttpResponse<IBooksPage>) => {
          if (booksPage.body) {
            return of(booksPage.body);
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
