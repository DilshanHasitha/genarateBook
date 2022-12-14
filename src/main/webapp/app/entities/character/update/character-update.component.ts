import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { CharacterFormService, CharacterFormGroup } from './character-form.service';
import { ICharacter } from '../character.model';
import { CharacterService } from '../service/character.service';

@Component({
  selector: 'jhi-character-update',
  templateUrl: './character-update.component.html',
})
export class CharacterUpdateComponent implements OnInit {
  isSaving = false;
  character: ICharacter | null = null;

  editForm: CharacterFormGroup = this.characterFormService.createCharacterFormGroup();

  constructor(
    protected characterService: CharacterService,
    protected characterFormService: CharacterFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ character }) => {
      this.character = character;
      if (character) {
        this.updateForm(character);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const character = this.characterFormService.getCharacter(this.editForm);
    if (character.id !== null) {
      this.subscribeToSaveResponse(this.characterService.update(character));
    } else {
      this.subscribeToSaveResponse(this.characterService.create(character));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICharacter>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(character: ICharacter): void {
    this.character = character;
    this.characterFormService.resetForm(this.editForm, character);
  }
}
