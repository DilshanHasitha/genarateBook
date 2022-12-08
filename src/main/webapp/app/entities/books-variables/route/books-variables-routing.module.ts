import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BooksVariablesComponent } from '../list/books-variables.component';
import { BooksVariablesDetailComponent } from '../detail/books-variables-detail.component';
import { BooksVariablesUpdateComponent } from '../update/books-variables-update.component';
import { BooksVariablesRoutingResolveService } from './books-variables-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const booksVariablesRoute: Routes = [
  {
    path: '',
    component: BooksVariablesComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BooksVariablesDetailComponent,
    resolve: {
      booksVariables: BooksVariablesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BooksVariablesUpdateComponent,
    resolve: {
      booksVariables: BooksVariablesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BooksVariablesUpdateComponent,
    resolve: {
      booksVariables: BooksVariablesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(booksVariablesRoute)],
  exports: [RouterModule],
})
export class BooksVariablesRoutingModule {}
