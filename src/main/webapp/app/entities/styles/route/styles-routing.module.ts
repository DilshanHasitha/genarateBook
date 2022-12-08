import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StylesComponent } from '../list/styles.component';
import { StylesDetailComponent } from '../detail/styles-detail.component';
import { StylesUpdateComponent } from '../update/styles-update.component';
import { StylesRoutingResolveService } from './styles-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const stylesRoute: Routes = [
  {
    path: '',
    component: StylesComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StylesDetailComponent,
    resolve: {
      styles: StylesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StylesUpdateComponent,
    resolve: {
      styles: StylesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StylesUpdateComponent,
    resolve: {
      styles: StylesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(stylesRoute)],
  exports: [RouterModule],
})
export class StylesRoutingModule {}
