import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SelectedOptionFormService, SelectedOptionFormGroup } from './selected-option-form.service';
import { ISelectedOption } from '../selected-option.model';
import { SelectedOptionService } from '../service/selected-option.service';
import { IBooks } from 'app/entities/books/books.model';
import { BooksService } from 'app/entities/books/service/books.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { ISelectedOptionDetails } from 'app/entities/selected-option-details/selected-option-details.model';
import { SelectedOptionDetailsService } from 'app/entities/selected-option-details/service/selected-option-details.service';

@Component({
  selector: 'jhi-selected-option-update',
  templateUrl: './selected-option-update.component.html',
})
export class SelectedOptionUpdateComponent implements OnInit {
  isSaving = false;
  selectedOption: ISelectedOption | null = null;

  booksSharedCollection: IBooks[] = [];
  customersSharedCollection: ICustomer[] = [];
  selectedOptionDetailsSharedCollection: ISelectedOptionDetails[] = [];

  editForm: SelectedOptionFormGroup = this.selectedOptionFormService.createSelectedOptionFormGroup();

  constructor(
    protected selectedOptionService: SelectedOptionService,
    protected selectedOptionFormService: SelectedOptionFormService,
    protected booksService: BooksService,
    protected customerService: CustomerService,
    protected selectedOptionDetailsService: SelectedOptionDetailsService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareBooks = (o1: IBooks | null, o2: IBooks | null): boolean => this.booksService.compareBooks(o1, o2);

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  compareSelectedOptionDetails = (o1: ISelectedOptionDetails | null, o2: ISelectedOptionDetails | null): boolean =>
    this.selectedOptionDetailsService.compareSelectedOptionDetails(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ selectedOption }) => {
      this.selectedOption = selectedOption;
      if (selectedOption) {
        this.updateForm(selectedOption);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const selectedOption = this.selectedOptionFormService.getSelectedOption(this.editForm);
    if (selectedOption.id !== null) {
      this.subscribeToSaveResponse(this.selectedOptionService.update(selectedOption));
    } else {
      this.subscribeToSaveResponse(this.selectedOptionService.create(selectedOption));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISelectedOption>>): void {
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

  protected updateForm(selectedOption: ISelectedOption): void {
    this.selectedOption = selectedOption;
    this.selectedOptionFormService.resetForm(this.editForm, selectedOption);

    this.booksSharedCollection = this.booksService.addBooksToCollectionIfMissing<IBooks>(this.booksSharedCollection, selectedOption.books);
    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
      this.customersSharedCollection,
      selectedOption.customer
    );
    this.selectedOptionDetailsSharedCollection =
      this.selectedOptionDetailsService.addSelectedOptionDetailsToCollectionIfMissing<ISelectedOptionDetails>(
        this.selectedOptionDetailsSharedCollection,
        ...(selectedOption.selectedOptionDetails ?? [])
      );
  }

  protected loadRelationshipsOptions(): void {
    this.booksService
      .query()
      .pipe(map((res: HttpResponse<IBooks[]>) => res.body ?? []))
      .pipe(map((books: IBooks[]) => this.booksService.addBooksToCollectionIfMissing<IBooks>(books, this.selectedOption?.books)))
      .subscribe((books: IBooks[]) => (this.booksSharedCollection = books));

    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) =>
          this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, this.selectedOption?.customer)
        )
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));

    this.selectedOptionDetailsService
      .query()
      .pipe(map((res: HttpResponse<ISelectedOptionDetails[]>) => res.body ?? []))
      .pipe(
        map((selectedOptionDetails: ISelectedOptionDetails[]) =>
          this.selectedOptionDetailsService.addSelectedOptionDetailsToCollectionIfMissing<ISelectedOptionDetails>(
            selectedOptionDetails,
            ...(this.selectedOption?.selectedOptionDetails ?? [])
          )
        )
      )
      .subscribe((selectedOptionDetails: ISelectedOptionDetails[]) => (this.selectedOptionDetailsSharedCollection = selectedOptionDetails));
  }
}
