import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILayers } from '../layers.model';
import { LayersService } from '../service/layers.service';

@Injectable({ providedIn: 'root' })
export class LayersRoutingResolveService implements Resolve<ILayers | null> {
  constructor(protected service: LayersService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILayers | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((layers: HttpResponse<ILayers>) => {
          if (layers.body) {
            return of(layers.body);
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
