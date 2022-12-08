import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILayerGroup } from '../layer-group.model';
import { LayerGroupService } from '../service/layer-group.service';

@Injectable({ providedIn: 'root' })
export class LayerGroupRoutingResolveService implements Resolve<ILayerGroup | null> {
  constructor(protected service: LayerGroupService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILayerGroup | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((layerGroup: HttpResponse<ILayerGroup>) => {
          if (layerGroup.body) {
            return of(layerGroup.body);
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
