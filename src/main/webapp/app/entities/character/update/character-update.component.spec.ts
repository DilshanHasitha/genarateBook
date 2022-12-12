import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CharacterFormService } from './character-form.service';
import { CharacterService } from '../service/character.service';
import { ICharacter } from '../character.model';
import { IAvatarCharactor } from 'app/entities/avatar-charactor/avatar-charactor.model';
import { AvatarCharactorService } from 'app/entities/avatar-charactor/service/avatar-charactor.service';

import { CharacterUpdateComponent } from './character-update.component';

describe('Character Management Update Component', () => {
  let comp: CharacterUpdateComponent;
  let fixture: ComponentFixture<CharacterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let characterFormService: CharacterFormService;
  let characterService: CharacterService;
  let avatarCharactorService: AvatarCharactorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CharacterUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CharacterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CharacterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    characterFormService = TestBed.inject(CharacterFormService);
    characterService = TestBed.inject(CharacterService);
    avatarCharactorService = TestBed.inject(AvatarCharactorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AvatarCharactor query and add missing value', () => {
      const character: ICharacter = { id: 456 };
      const avatarCharactors: IAvatarCharactor[] = [{ id: 92140 }];
      character.avatarCharactors = avatarCharactors;

      const avatarCharactorCollection: IAvatarCharactor[] = [{ id: 98096 }];
      jest.spyOn(avatarCharactorService, 'query').mockReturnValue(of(new HttpResponse({ body: avatarCharactorCollection })));
      const additionalAvatarCharactors = [...avatarCharactors];
      const expectedCollection: IAvatarCharactor[] = [...additionalAvatarCharactors, ...avatarCharactorCollection];
      jest.spyOn(avatarCharactorService, 'addAvatarCharactorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ character });
      comp.ngOnInit();

      expect(avatarCharactorService.query).toHaveBeenCalled();
      expect(avatarCharactorService.addAvatarCharactorToCollectionIfMissing).toHaveBeenCalledWith(
        avatarCharactorCollection,
        ...additionalAvatarCharactors.map(expect.objectContaining)
      );
      expect(comp.avatarCharactorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const character: ICharacter = { id: 456 };
      const avatarCharactor: IAvatarCharactor = { id: 19459 };
      character.avatarCharactors = [avatarCharactor];

      activatedRoute.data = of({ character });
      comp.ngOnInit();

      expect(comp.avatarCharactorsSharedCollection).toContain(avatarCharactor);
      expect(comp.character).toEqual(character);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICharacter>>();
      const character = { id: 123 };
      jest.spyOn(characterFormService, 'getCharacter').mockReturnValue(character);
      jest.spyOn(characterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ character });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: character }));
      saveSubject.complete();

      // THEN
      expect(characterFormService.getCharacter).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(characterService.update).toHaveBeenCalledWith(expect.objectContaining(character));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICharacter>>();
      const character = { id: 123 };
      jest.spyOn(characterFormService, 'getCharacter').mockReturnValue({ id: null });
      jest.spyOn(characterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ character: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: character }));
      saveSubject.complete();

      // THEN
      expect(characterFormService.getCharacter).toHaveBeenCalled();
      expect(characterService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICharacter>>();
      const character = { id: 123 };
      jest.spyOn(characterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ character });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(characterService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAvatarCharactor', () => {
      it('Should forward to avatarCharactorService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(avatarCharactorService, 'compareAvatarCharactor');
        comp.compareAvatarCharactor(entity, entity2);
        expect(avatarCharactorService.compareAvatarCharactor).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
