import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AvatarAttributesFormService } from './avatar-attributes-form.service';
import { AvatarAttributesService } from '../service/avatar-attributes.service';
import { IAvatarAttributes } from '../avatar-attributes.model';
import { IAvatarCharactor } from 'app/entities/avatar-charactor/avatar-charactor.model';
import { AvatarCharactorService } from 'app/entities/avatar-charactor/service/avatar-charactor.service';
import { IOptions } from 'app/entities/options/options.model';
import { OptionsService } from 'app/entities/options/service/options.service';

import { AvatarAttributesUpdateComponent } from './avatar-attributes-update.component';

describe('AvatarAttributes Management Update Component', () => {
  let comp: AvatarAttributesUpdateComponent;
  let fixture: ComponentFixture<AvatarAttributesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let avatarAttributesFormService: AvatarAttributesFormService;
  let avatarAttributesService: AvatarAttributesService;
  let avatarCharactorService: AvatarCharactorService;
  let optionsService: OptionsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AvatarAttributesUpdateComponent],
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
      .overrideTemplate(AvatarAttributesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AvatarAttributesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    avatarAttributesFormService = TestBed.inject(AvatarAttributesFormService);
    avatarAttributesService = TestBed.inject(AvatarAttributesService);
    avatarCharactorService = TestBed.inject(AvatarCharactorService);
    optionsService = TestBed.inject(OptionsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AvatarCharactor query and add missing value', () => {
      const avatarAttributes: IAvatarAttributes = { id: 456 };
      const avatarCharactors: IAvatarCharactor[] = [{ id: 76934 }];
      avatarAttributes.avatarCharactors = avatarCharactors;

      const avatarCharactorCollection: IAvatarCharactor[] = [{ id: 75058 }];
      jest.spyOn(avatarCharactorService, 'query').mockReturnValue(of(new HttpResponse({ body: avatarCharactorCollection })));
      const additionalAvatarCharactors = [...avatarCharactors];
      const expectedCollection: IAvatarCharactor[] = [...additionalAvatarCharactors, ...avatarCharactorCollection];
      jest.spyOn(avatarCharactorService, 'addAvatarCharactorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ avatarAttributes });
      comp.ngOnInit();

      expect(avatarCharactorService.query).toHaveBeenCalled();
      expect(avatarCharactorService.addAvatarCharactorToCollectionIfMissing).toHaveBeenCalledWith(
        avatarCharactorCollection,
        ...additionalAvatarCharactors.map(expect.objectContaining)
      );
      expect(comp.avatarCharactorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Options query and add missing value', () => {
      const avatarAttributes: IAvatarAttributes = { id: 456 };
      const options: IOptions[] = [{ id: 27830 }];
      avatarAttributes.options = options;

      const optionsCollection: IOptions[] = [{ id: 25470 }];
      jest.spyOn(optionsService, 'query').mockReturnValue(of(new HttpResponse({ body: optionsCollection })));
      const additionalOptions = [...options];
      const expectedCollection: IOptions[] = [...additionalOptions, ...optionsCollection];
      jest.spyOn(optionsService, 'addOptionsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ avatarAttributes });
      comp.ngOnInit();

      expect(optionsService.query).toHaveBeenCalled();
      expect(optionsService.addOptionsToCollectionIfMissing).toHaveBeenCalledWith(
        optionsCollection,
        ...additionalOptions.map(expect.objectContaining)
      );
      expect(comp.optionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const avatarAttributes: IAvatarAttributes = { id: 456 };
      const avatarCharactor: IAvatarCharactor = { id: 6430 };
      avatarAttributes.avatarCharactors = [avatarCharactor];
      const option: IOptions = { id: 66925 };
      avatarAttributes.options = [option];

      activatedRoute.data = of({ avatarAttributes });
      comp.ngOnInit();

      expect(comp.avatarCharactorsSharedCollection).toContain(avatarCharactor);
      expect(comp.optionsSharedCollection).toContain(option);
      expect(comp.avatarAttributes).toEqual(avatarAttributes);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAvatarAttributes>>();
      const avatarAttributes = { id: 123 };
      jest.spyOn(avatarAttributesFormService, 'getAvatarAttributes').mockReturnValue(avatarAttributes);
      jest.spyOn(avatarAttributesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ avatarAttributes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: avatarAttributes }));
      saveSubject.complete();

      // THEN
      expect(avatarAttributesFormService.getAvatarAttributes).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(avatarAttributesService.update).toHaveBeenCalledWith(expect.objectContaining(avatarAttributes));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAvatarAttributes>>();
      const avatarAttributes = { id: 123 };
      jest.spyOn(avatarAttributesFormService, 'getAvatarAttributes').mockReturnValue({ id: null });
      jest.spyOn(avatarAttributesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ avatarAttributes: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: avatarAttributes }));
      saveSubject.complete();

      // THEN
      expect(avatarAttributesFormService.getAvatarAttributes).toHaveBeenCalled();
      expect(avatarAttributesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAvatarAttributes>>();
      const avatarAttributes = { id: 123 };
      jest.spyOn(avatarAttributesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ avatarAttributes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(avatarAttributesService.update).toHaveBeenCalled();
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

    describe('compareOptions', () => {
      it('Should forward to optionsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(optionsService, 'compareOptions');
        comp.compareOptions(entity, entity2);
        expect(optionsService.compareOptions).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
