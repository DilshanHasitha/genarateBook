import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AvatarCharactorFormService } from './avatar-charactor-form.service';
import { AvatarCharactorService } from '../service/avatar-charactor.service';
import { IAvatarCharactor } from '../avatar-charactor.model';
import { ILayerGroup } from 'app/entities/layer-group/layer-group.model';
import { LayerGroupService } from 'app/entities/layer-group/service/layer-group.service';
import { ICharacter } from 'app/entities/character/character.model';
import { CharacterService } from 'app/entities/character/service/character.service';

import { AvatarCharactorUpdateComponent } from './avatar-charactor-update.component';

describe('AvatarCharactor Management Update Component', () => {
  let comp: AvatarCharactorUpdateComponent;
  let fixture: ComponentFixture<AvatarCharactorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let avatarCharactorFormService: AvatarCharactorFormService;
  let avatarCharactorService: AvatarCharactorService;
  let layerGroupService: LayerGroupService;
  let characterService: CharacterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AvatarCharactorUpdateComponent],
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
      .overrideTemplate(AvatarCharactorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AvatarCharactorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    avatarCharactorFormService = TestBed.inject(AvatarCharactorFormService);
    avatarCharactorService = TestBed.inject(AvatarCharactorService);
    layerGroupService = TestBed.inject(LayerGroupService);
    characterService = TestBed.inject(CharacterService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call LayerGroup query and add missing value', () => {
      const avatarCharactor: IAvatarCharactor = { id: 456 };
      const layerGroup: ILayerGroup = { id: 78146 };
      avatarCharactor.layerGroup = layerGroup;

      const layerGroupCollection: ILayerGroup[] = [{ id: 71222 }];
      jest.spyOn(layerGroupService, 'query').mockReturnValue(of(new HttpResponse({ body: layerGroupCollection })));
      const additionalLayerGroups = [layerGroup];
      const expectedCollection: ILayerGroup[] = [...additionalLayerGroups, ...layerGroupCollection];
      jest.spyOn(layerGroupService, 'addLayerGroupToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ avatarCharactor });
      comp.ngOnInit();

      expect(layerGroupService.query).toHaveBeenCalled();
      expect(layerGroupService.addLayerGroupToCollectionIfMissing).toHaveBeenCalledWith(
        layerGroupCollection,
        ...additionalLayerGroups.map(expect.objectContaining)
      );
      expect(comp.layerGroupsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Character query and add missing value', () => {
      const avatarCharactor: IAvatarCharactor = { id: 456 };
      const character: ICharacter = { id: 93981 };
      avatarCharactor.character = character;

      const characterCollection: ICharacter[] = [{ id: 7544 }];
      jest.spyOn(characterService, 'query').mockReturnValue(of(new HttpResponse({ body: characterCollection })));
      const additionalCharacters = [character];
      const expectedCollection: ICharacter[] = [...additionalCharacters, ...characterCollection];
      jest.spyOn(characterService, 'addCharacterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ avatarCharactor });
      comp.ngOnInit();

      expect(characterService.query).toHaveBeenCalled();
      expect(characterService.addCharacterToCollectionIfMissing).toHaveBeenCalledWith(
        characterCollection,
        ...additionalCharacters.map(expect.objectContaining)
      );
      expect(comp.charactersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const avatarCharactor: IAvatarCharactor = { id: 456 };
      const layerGroup: ILayerGroup = { id: 94968 };
      avatarCharactor.layerGroup = layerGroup;
      const character: ICharacter = { id: 23048 };
      avatarCharactor.character = character;

      activatedRoute.data = of({ avatarCharactor });
      comp.ngOnInit();

      expect(comp.layerGroupsSharedCollection).toContain(layerGroup);
      expect(comp.charactersSharedCollection).toContain(character);
      expect(comp.avatarCharactor).toEqual(avatarCharactor);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAvatarCharactor>>();
      const avatarCharactor = { id: 123 };
      jest.spyOn(avatarCharactorFormService, 'getAvatarCharactor').mockReturnValue(avatarCharactor);
      jest.spyOn(avatarCharactorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ avatarCharactor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: avatarCharactor }));
      saveSubject.complete();

      // THEN
      expect(avatarCharactorFormService.getAvatarCharactor).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(avatarCharactorService.update).toHaveBeenCalledWith(expect.objectContaining(avatarCharactor));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAvatarCharactor>>();
      const avatarCharactor = { id: 123 };
      jest.spyOn(avatarCharactorFormService, 'getAvatarCharactor').mockReturnValue({ id: null });
      jest.spyOn(avatarCharactorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ avatarCharactor: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: avatarCharactor }));
      saveSubject.complete();

      // THEN
      expect(avatarCharactorFormService.getAvatarCharactor).toHaveBeenCalled();
      expect(avatarCharactorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAvatarCharactor>>();
      const avatarCharactor = { id: 123 };
      jest.spyOn(avatarCharactorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ avatarCharactor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(avatarCharactorService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareLayerGroup', () => {
      it('Should forward to layerGroupService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(layerGroupService, 'compareLayerGroup');
        comp.compareLayerGroup(entity, entity2);
        expect(layerGroupService.compareLayerGroup).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCharacter', () => {
      it('Should forward to characterService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(characterService, 'compareCharacter');
        comp.compareCharacter(entity, entity2);
        expect(characterService.compareCharacter).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
