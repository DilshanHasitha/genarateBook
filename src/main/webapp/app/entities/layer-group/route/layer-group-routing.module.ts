import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LayerGroupComponent } from '../list/layer-group.component';
import { LayerGroupDetailComponent } from '../detail/layer-group-detail.component';
import { LayerGroupUpdateComponent } from '../update/layer-group-update.component';
import { LayerGroupRoutingResolveService } from './layer-group-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const layerGroupRoute: Routes = [
  {
    path: '',
    component: LayerGroupComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LayerGroupDetailComponent,
    resolve: {
      layerGroup: LayerGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LayerGroupUpdateComponent,
    resolve: {
      layerGroup: LayerGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LayerGroupUpdateComponent,
    resolve: {
      layerGroup: LayerGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(layerGroupRoute)],
  exports: [RouterModule],
})
export class LayerGroupRoutingModule {}
