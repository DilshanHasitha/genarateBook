import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LayerDetailsComponent } from '../list/layer-details.component';
import { LayerDetailsDetailComponent } from '../detail/layer-details-detail.component';
import { LayerDetailsUpdateComponent } from '../update/layer-details-update.component';
import { LayerDetailsRoutingResolveService } from './layer-details-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const layerDetailsRoute: Routes = [
  {
    path: '',
    component: LayerDetailsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LayerDetailsDetailComponent,
    resolve: {
      layerDetails: LayerDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LayerDetailsUpdateComponent,
    resolve: {
      layerDetails: LayerDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LayerDetailsUpdateComponent,
    resolve: {
      layerDetails: LayerDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(layerDetailsRoute)],
  exports: [RouterModule],
})
export class LayerDetailsRoutingModule {}
