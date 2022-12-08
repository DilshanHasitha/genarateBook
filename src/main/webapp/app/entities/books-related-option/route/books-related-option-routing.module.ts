import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BooksRelatedOptionComponent } from '../list/books-related-option.component';
import { BooksRelatedOptionDetailComponent } from '../detail/books-related-option-detail.component';
import { BooksRelatedOptionUpdateComponent } from '../update/books-related-option-update.component';
import { BooksRelatedOptionRoutingResolveService } from './books-related-option-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const booksRelatedOptionRoute: Routes = [
  {
    path: '',
    component: BooksRelatedOptionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BooksRelatedOptionDetailComponent,
    resolve: {
      booksRelatedOption: BooksRelatedOptionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BooksRelatedOptionUpdateComponent,
    resolve: {
      booksRelatedOption: BooksRelatedOptionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BooksRelatedOptionUpdateComponent,
    resolve: {
      booksRelatedOption: BooksRelatedOptionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(booksRelatedOptionRoute)],
  exports: [RouterModule],
})
export class BooksRelatedOptionRoutingModule {}
