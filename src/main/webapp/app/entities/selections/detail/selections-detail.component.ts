import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISelections } from '../selections.model';

@Component({
  selector: 'jhi-selections-detail',
  templateUrl: './selections-detail.component.html',
})
export class SelectionsDetailComponent implements OnInit {
  selections: ISelections | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ selections }) => {
      this.selections = selections;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
