import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICharacter } from '../character.model';

@Component({
  selector: 'jhi-character-detail',
  templateUrl: './character-detail.component.html',
})
export class CharacterDetailComponent implements OnInit {
  character: ICharacter | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ character }) => {
      this.character = character;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
