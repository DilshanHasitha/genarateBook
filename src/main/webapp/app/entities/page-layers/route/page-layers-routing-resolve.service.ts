import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPageLayers } from '../page-layers.model';
import { PageLayersService } from '../service/page-layers.service';

@Injectable({ providedIn: 'root' })
export class PageLayersRoutingResolveService implements Resolve<IPageLayers | null> {
  constructor(protected service: PageLayersService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPageLayers | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pageLayers: HttpResponse<IPageLayers>) => {
          if (pageLayers.body) {
            return of(pageLayers.body);
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
