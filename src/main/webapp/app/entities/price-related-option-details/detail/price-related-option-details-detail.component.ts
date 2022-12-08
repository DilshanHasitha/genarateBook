import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPriceRelatedOptionDetails } from '../price-related-option-details.model';

@Component({
  selector: 'jhi-price-related-option-details-detail',
  templateUrl: './price-related-option-details-detail.component.html',
})
export class PriceRelatedOptionDetailsDetailComponent implements OnInit {
  priceRelatedOptionDetails: IPriceRelatedOptionDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ priceRelatedOptionDetails }) => {
      this.priceRelatedOptionDetails = priceRelatedOptionDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
