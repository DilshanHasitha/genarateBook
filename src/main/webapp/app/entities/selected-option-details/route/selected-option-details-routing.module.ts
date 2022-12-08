import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SelectedOptionDetailsComponent } from '../list/selected-option-details.component';
import { SelectedOptionDetailsDetailComponent } from '../detail/selected-option-details-detail.component';
import { SelectedOptionDetailsUpdateComponent } from '../update/selected-option-details-update.component';
import { SelectedOptionDetailsRoutingResolveService } from './selected-option-details-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const selectedOptionDetailsRoute: Routes = [
  {
    path: '',
    component: SelectedOptionDetailsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SelectedOptionDetailsDetailComponent,
    resolve: {
      selectedOptionDetails: SelectedOptionDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SelectedOptionDetailsUpdateComponent,
    resolve: {
      selectedOptionDetails: SelectedOptionDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SelectedOptionDetailsUpdateComponent,
    resolve: {
      selectedOptionDetails: SelectedOptionDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(selectedOptionDetailsRoute)],
  exports: [RouterModule],
})
export class SelectedOptionDetailsRoutingModule {}
