import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBooks } from '../books.model';
import { BooksService } from '../service/books.service';

@Injectable({ providedIn: 'root' })
export class BooksRoutingResolveService implements Resolve<IBooks | null> {
  constructor(protected service: BooksService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBooks | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((books: HttpResponse<IBooks>) => {
          if (books.body) {
            return of(books.body);
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
