import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SelectedOptionFormService } from './selected-option-form.service';
import { SelectedOptionService } from '../service/selected-option.service';
import { ISelectedOption } from '../selected-option.model';
import { IBooks } from 'app/entities/books/books.model';
import { BooksService } from 'app/entities/books/service/books.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { ISelectedOptionDetails } from 'app/entities/selected-option-details/selected-option-details.model';
import { SelectedOptionDetailsService } from 'app/entities/selected-option-details/service/selected-option-details.service';

import { SelectedOptionUpdateComponent } from './selected-option-update.component';

describe('SelectedOption Management Update Component', () => {
  let comp: SelectedOptionUpdateComponent;
  let fixture: ComponentFixture<SelectedOptionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let selectedOptionFormService: SelectedOptionFormService;
  let selectedOptionService: SelectedOptionService;
  let booksService: BooksService;
  let customerService: CustomerService;
  let selectedOptionDetailsService: SelectedOptionDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SelectedOptionUpdateComponent],
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
      .overrideTemplate(SelectedOptionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SelectedOptionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    selectedOptionFormService = TestBed.inject(SelectedOptionFormService);
    selectedOptionService = TestBed.inject(SelectedOptionService);
    booksService = TestBed.inject(BooksService);
    customerService = TestBed.inject(CustomerService);
    selectedOptionDetailsService = TestBed.inject(SelectedOptionDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Books query and add missing value', () => {
      const selectedOption: ISelectedOption = { id: 456 };
      const books: IBooks = { id: 10140 };
      selectedOption.books = books;

      const booksCollection: IBooks[] = [{ id: 76928 }];
      jest.spyOn(booksService, 'query').mockReturnValue(of(new HttpResponse({ body: booksCollection })));
      const additionalBooks = [books];
      const expectedCollection: IBooks[] = [...additionalBooks, ...booksCollection];
      jest.spyOn(booksService, 'addBooksToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ selectedOption });
      comp.ngOnInit();

      expect(booksService.query).toHaveBeenCalled();
      expect(booksService.addBooksToCollectionIfMissing).toHaveBeenCalledWith(
        booksCollection,
        ...additionalBooks.map(expect.objectContaining)
      );
      expect(comp.booksSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Customer query and add missing value', () => {
      const selectedOption: ISelectedOption = { id: 456 };
      const customer: ICustomer = { id: 59712 };
      selectedOption.customer = customer;

      const customerCollection: ICustomer[] = [{ id: 5808 }];
      jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
      const additionalCustomers = [customer];
      const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
      jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ selectedOption });
      comp.ngOnInit();

      expect(customerService.query).toHaveBeenCalled();
      expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        customerCollection,
        ...additionalCustomers.map(expect.objectContaining)
      );
      expect(comp.customersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SelectedOptionDetails query and add missing value', () => {
      const selectedOption: ISelectedOption = { id: 456 };
      const selectedOptionDetails: ISelectedOptionDetails[] = [{ id: 5518 }];
      selectedOption.selectedOptionDetails = selectedOptionDetails;

      const selectedOptionDetailsCollection: ISelectedOptionDetails[] = [{ id: 22981 }];
      jest.spyOn(selectedOptionDetailsService, 'query').mockReturnValue(of(new HttpResponse({ body: selectedOptionDetailsCollection })));
      const additionalSelectedOptionDetails = [...selectedOptionDetails];
      const expectedCollection: ISelectedOptionDetails[] = [...additionalSelectedOptionDetails, ...selectedOptionDetailsCollection];
      jest.spyOn(selectedOptionDetailsService, 'addSelectedOptionDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ selectedOption });
      comp.ngOnInit();

      expect(selectedOptionDetailsService.query).toHaveBeenCalled();
      expect(selectedOptionDetailsService.addSelectedOptionDetailsToCollectionIfMissing).toHaveBeenCalledWith(
        selectedOptionDetailsCollection,
        ...additionalSelectedOptionDetails.map(expect.objectContaining)
      );
      expect(comp.selectedOptionDetailsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const selectedOption: ISelectedOption = { id: 456 };
      const books: IBooks = { id: 32491 };
      selectedOption.books = books;
      const customer: ICustomer = { id: 1491 };
      selectedOption.customer = customer;
      const selectedOptionDetails: ISelectedOptionDetails = { id: 11633 };
      selectedOption.selectedOptionDetails = [selectedOptionDetails];

      activatedRoute.data = of({ selectedOption });
      comp.ngOnInit();

      expect(comp.booksSharedCollection).toContain(books);
      expect(comp.customersSharedCollection).toContain(customer);
      expect(comp.selectedOptionDetailsSharedCollection).toContain(selectedOptionDetails);
      expect(comp.selectedOption).toEqual(selectedOption);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISelectedOption>>();
      const selectedOption = { id: 123 };
      jest.spyOn(selectedOptionFormService, 'getSelectedOption').mockReturnValue(selectedOption);
      jest.spyOn(selectedOptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ selectedOption });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: selectedOption }));
      saveSubject.complete();

      // THEN
      expect(selectedOptionFormService.getSelectedOption).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(selectedOptionService.update).toHaveBeenCalledWith(expect.objectContaining(selectedOption));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISelectedOption>>();
      const selectedOption = { id: 123 };
      jest.spyOn(selectedOptionFormService, 'getSelectedOption').mockReturnValue({ id: null });
      jest.spyOn(selectedOptionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ selectedOption: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: selectedOption }));
      saveSubject.complete();

      // THEN
      expect(selectedOptionFormService.getSelectedOption).toHaveBeenCalled();
      expect(selectedOptionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISelectedOption>>();
      const selectedOption = { id: 123 };
      jest.spyOn(selectedOptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ selectedOption });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(selectedOptionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareBooks', () => {
      it('Should forward to booksService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(booksService, 'compareBooks');
        comp.compareBooks(entity, entity2);
        expect(booksService.compareBooks).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCustomer', () => {
      it('Should forward to customerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(customerService, 'compareCustomer');
        comp.compareCustomer(entity, entity2);
        expect(customerService.compareCustomer).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSelectedOptionDetails', () => {
      it('Should forward to selectedOptionDetailsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(selectedOptionDetailsService, 'compareSelectedOptionDetails');
        comp.compareSelectedOptionDetails(entity, entity2);
        expect(selectedOptionDetailsService.compareSelectedOptionDetails).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
