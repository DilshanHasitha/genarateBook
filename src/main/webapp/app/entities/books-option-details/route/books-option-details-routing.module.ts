import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BooksOptionDetailsComponent } from '../list/books-option-details.component';
import { BooksOptionDetailsDetailComponent } from '../detail/books-option-details-detail.component';
import { BooksOptionDetailsUpdateComponent } from '../update/books-option-details-update.component';
import { BooksOptionDetailsRoutingResolveService } from './books-option-details-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const booksOptionDetailsRoute: Routes = [
  {
    path: '',
    component: BooksOptionDetailsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BooksOptionDetailsDetailComponent,
    resolve: {
      booksOptionDetails: BooksOptionDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BooksOptionDetailsUpdateComponent,
    resolve: {
      booksOptionDetails: BooksOptionDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BooksOptionDetailsUpdateComponent,
    resolve: {
      booksOptionDetails: BooksOptionDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(booksOptionDetailsRoute)],
  exports: [RouterModule],
})
export class BooksOptionDetailsRoutingModule {}
