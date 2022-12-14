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

import { CharacterUpdateComponent } from './character-update.component';

describe('Character Management Update Component', () => {
  let comp: CharacterUpdateComponent;
  let fixture: ComponentFixture<CharacterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let characterFormService: CharacterFormService;
  let characterService: CharacterService;

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

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const character: ICharacter = { id: 456 };

      activatedRoute.data = of({ character });
      comp.ngOnInit();

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
});
