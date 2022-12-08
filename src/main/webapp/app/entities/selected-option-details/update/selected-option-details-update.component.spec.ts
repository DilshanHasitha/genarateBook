import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SelectedOptionDetailsFormService } from './selected-option-details-form.service';
import { SelectedOptionDetailsService } from '../service/selected-option-details.service';
import { ISelectedOptionDetails } from '../selected-option-details.model';

import { SelectedOptionDetailsUpdateComponent } from './selected-option-details-update.component';

describe('SelectedOptionDetails Management Update Component', () => {
  let comp: SelectedOptionDetailsUpdateComponent;
  let fixture: ComponentFixture<SelectedOptionDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let selectedOptionDetailsFormService: SelectedOptionDetailsFormService;
  let selectedOptionDetailsService: SelectedOptionDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SelectedOptionDetailsUpdateComponent],
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
      .overrideTemplate(SelectedOptionDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SelectedOptionDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    selectedOptionDetailsFormService = TestBed.inject(SelectedOptionDetailsFormService);
    selectedOptionDetailsService = TestBed.inject(SelectedOptionDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const selectedOptionDetails: ISelectedOptionDetails = { id: 456 };

      activatedRoute.data = of({ selectedOptionDetails });
      comp.ngOnInit();

      expect(comp.selectedOptionDetails).toEqual(selectedOptionDetails);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISelectedOptionDetails>>();
      const selectedOptionDetails = { id: 123 };
      jest.spyOn(selectedOptionDetailsFormService, 'getSelectedOptionDetails').mockReturnValue(selectedOptionDetails);
      jest.spyOn(selectedOptionDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ selectedOptionDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: selectedOptionDetails }));
      saveSubject.complete();

      // THEN
      expect(selectedOptionDetailsFormService.getSelectedOptionDetails).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(selectedOptionDetailsService.update).toHaveBeenCalledWith(expect.objectContaining(selectedOptionDetails));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISelectedOptionDetails>>();
      const selectedOptionDetails = { id: 123 };
      jest.spyOn(selectedOptionDetailsFormService, 'getSelectedOptionDetails').mockReturnValue({ id: null });
      jest.spyOn(selectedOptionDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ selectedOptionDetails: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: selectedOptionDetails }));
      saveSubject.complete();

      // THEN
      expect(selectedOptionDetailsFormService.getSelectedOptionDetails).toHaveBeenCalled();
      expect(selectedOptionDetailsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISelectedOptionDetails>>();
      const selectedOptionDetails = { id: 123 };
      jest.spyOn(selectedOptionDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ selectedOptionDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(selectedOptionDetailsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
