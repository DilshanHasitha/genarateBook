import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBooksRelatedOptionDetails } from '../books-related-option-details.model';

@Component({
  selector: 'jhi-books-related-option-details-detail',
  templateUrl: './books-related-option-details-detail.component.html',
})
export class BooksRelatedOptionDetailsDetailComponent implements OnInit {
  booksRelatedOptionDetails: IBooksRelatedOptionDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ booksRelatedOptionDetails }) => {
      this.booksRelatedOptionDetails = booksRelatedOptionDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
