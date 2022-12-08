import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BooksRelatedOptionDetailsComponent } from '../list/books-related-option-details.component';
import { BooksRelatedOptionDetailsDetailComponent } from '../detail/books-related-option-details-detail.component';
import { BooksRelatedOptionDetailsUpdateComponent } from '../update/books-related-option-details-update.component';
import { BooksRelatedOptionDetailsRoutingResolveService } from './books-related-option-details-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const booksRelatedOptionDetailsRoute: Routes = [
  {
    path: '',
    component: BooksRelatedOptionDetailsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BooksRelatedOptionDetailsDetailComponent,
    resolve: {
      booksRelatedOptionDetails: BooksRelatedOptionDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BooksRelatedOptionDetailsUpdateComponent,
    resolve: {
      booksRelatedOptionDetails: BooksRelatedOptionDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BooksRelatedOptionDetailsUpdateComponent,
    resolve: {
      booksRelatedOptionDetails: BooksRelatedOptionDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(booksRelatedOptionDetailsRoute)],
  exports: [RouterModule],
})
export class BooksRelatedOptionDetailsRoutingModule {}
