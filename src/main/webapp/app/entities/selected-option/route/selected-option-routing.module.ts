import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SelectedOptionComponent } from '../list/selected-option.component';
import { SelectedOptionDetailComponent } from '../detail/selected-option-detail.component';
import { SelectedOptionUpdateComponent } from '../update/selected-option-update.component';
import { SelectedOptionRoutingResolveService } from './selected-option-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const selectedOptionRoute: Routes = [
  {
    path: '',
    component: SelectedOptionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SelectedOptionDetailComponent,
    resolve: {
      selectedOption: SelectedOptionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SelectedOptionUpdateComponent,
    resolve: {
      selectedOption: SelectedOptionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SelectedOptionUpdateComponent,
    resolve: {
      selectedOption: SelectedOptionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(selectedOptionRoute)],
  exports: [RouterModule],
})
export class SelectedOptionRoutingModule {}
