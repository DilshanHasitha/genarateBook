import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISelectedOptionDetails } from '../selected-option-details.model';

@Component({
  selector: 'jhi-selected-option-details-detail',
  templateUrl: './selected-option-details-detail.component.html',
})
export class SelectedOptionDetailsDetailComponent implements OnInit {
  selectedOptionDetails: ISelectedOptionDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ selectedOptionDetails }) => {
      this.selectedOptionDetails = selectedOptionDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
