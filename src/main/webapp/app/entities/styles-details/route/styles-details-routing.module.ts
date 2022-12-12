import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StylesDetailsComponent } from '../list/styles-details.component';
import { StylesDetailsDetailComponent } from '../detail/styles-details-detail.component';
import { StylesDetailsUpdateComponent } from '../update/styles-details-update.component';
import { StylesDetailsRoutingResolveService } from './styles-details-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const stylesDetailsRoute: Routes = [
  {
    path: '',
    component: StylesDetailsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StylesDetailsDetailComponent,
    resolve: {
      stylesDetails: StylesDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StylesDetailsUpdateComponent,
    resolve: {
      stylesDetails: StylesDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StylesDetailsUpdateComponent,
    resolve: {
      stylesDetails: StylesDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(stylesDetailsRoute)],
  exports: [RouterModule],
})
export class StylesDetailsRoutingModule {}
