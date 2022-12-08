import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PageSizeComponent } from '../list/page-size.component';
import { PageSizeDetailComponent } from '../detail/page-size-detail.component';
import { PageSizeUpdateComponent } from '../update/page-size-update.component';
import { PageSizeRoutingResolveService } from './page-size-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const pageSizeRoute: Routes = [
  {
    path: '',
    component: PageSizeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PageSizeDetailComponent,
    resolve: {
      pageSize: PageSizeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PageSizeUpdateComponent,
    resolve: {
      pageSize: PageSizeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PageSizeUpdateComponent,
    resolve: {
      pageSize: PageSizeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pageSizeRoute)],
  exports: [RouterModule],
})
export class PageSizeRoutingModule {}
