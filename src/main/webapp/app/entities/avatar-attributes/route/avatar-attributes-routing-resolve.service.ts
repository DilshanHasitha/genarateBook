import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAvatarAttributes } from '../avatar-attributes.model';
import { AvatarAttributesService } from '../service/avatar-attributes.service';

@Injectable({ providedIn: 'root' })
export class AvatarAttributesRoutingResolveService implements Resolve<IAvatarAttributes | null> {
  constructor(protected service: AvatarAttributesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAvatarAttributes | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((avatarAttributes: HttpResponse<IAvatarAttributes>) => {
          if (avatarAttributes.body) {
            return of(avatarAttributes.body);
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
