import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOptionType } from '../option-type.model';

@Component({
  selector: 'jhi-option-type-detail',
  templateUrl: './option-type-detail.component.html',
})
export class OptionTypeDetailComponent implements OnInit {
  optionType: IOptionType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ optionType }) => {
      this.optionType = optionType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
