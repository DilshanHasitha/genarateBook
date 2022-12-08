import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAvatarCharactor } from '../avatar-charactor.model';
import { AvatarCharactorService } from '../service/avatar-charactor.service';

@Injectable({ providedIn: 'root' })
export class AvatarCharactorRoutingResolveService implements Resolve<IAvatarCharactor | null> {
  constructor(protected service: AvatarCharactorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAvatarCharactor | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((avatarCharactor: HttpResponse<IAvatarCharactor>) => {
          if (avatarCharactor.body) {
            return of(avatarCharactor.body);
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
