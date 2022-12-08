import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBooksVariables } from '../books-variables.model';

@Component({
  selector: 'jhi-books-variables-detail',
  templateUrl: './books-variables-detail.component.html',
})
export class BooksVariablesDetailComponent implements OnInit {
  booksVariables: IBooksVariables | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ booksVariables }) => {
      this.booksVariables = booksVariables;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
