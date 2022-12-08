import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISelectedOption } from '../selected-option.model';

@Component({
  selector: 'jhi-selected-option-detail',
  templateUrl: './selected-option-detail.component.html',
})
export class SelectedOptionDetailComponent implements OnInit {
  selectedOption: ISelectedOption | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ selectedOption }) => {
      this.selectedOption = selectedOption;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
