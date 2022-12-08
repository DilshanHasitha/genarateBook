import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILayerDetails } from '../layer-details.model';
import { LayerDetailsService } from '../service/layer-details.service';

@Injectable({ providedIn: 'root' })
export class LayerDetailsRoutingResolveService implements Resolve<ILayerDetails | null> {
  constructor(protected service: LayerDetailsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILayerDetails | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((layerDetails: HttpResponse<ILayerDetails>) => {
          if (layerDetails.body) {
            return of(layerDetails.body);
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
