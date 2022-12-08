import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBooksOptionDetails } from '../books-option-details.model';

@Component({
  selector: 'jhi-books-option-details-detail',
  templateUrl: './books-option-details-detail.component.html',
})
export class BooksOptionDetailsDetailComponent implements OnInit {
  booksOptionDetails: IBooksOptionDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ booksOptionDetails }) => {
      this.booksOptionDetails = booksOptionDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
