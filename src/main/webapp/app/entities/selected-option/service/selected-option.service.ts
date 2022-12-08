import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISelectedOption, NewSelectedOption } from '../selected-option.model';

export type PartialUpdateSelectedOption = Partial<ISelectedOption> & Pick<ISelectedOption, 'id'>;

type RestOf<T extends ISelectedOption | NewSelectedOption> = Omit<T, 'date'> & {
  date?: string | null;
};

export type RestSelectedOption = RestOf<ISelectedOption>;

export type NewRestSelectedOption = RestOf<NewSelectedOption>;

export type PartialUpdateRestSelectedOption = RestOf<PartialUpdateSelectedOption>;

export type EntityResponseType = HttpResponse<ISelectedOption>;
export type EntityArrayResponseType = HttpResponse<ISelectedOption[]>;

@Injectable({ providedIn: 'root' })
export class SelectedOptionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/selected-options');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(selectedOption: NewSelectedOption): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(selectedOption);
    return this.http
      .post<RestSelectedOption>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(selectedOption: ISelectedOption): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(selectedOption);
    return this.http
      .put<RestSelectedOption>(`${this.resourceUrl}/${this.getSelectedOptionIdentifier(selectedOption)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(selectedOption: PartialUpdateSelectedOption): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(selectedOption);
    return this.http
      .patch<RestSelectedOption>(`${this.resourceUrl}/${this.getSelectedOptionIdentifier(selectedOption)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSelectedOption>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSelectedOption[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSelectedOptionIdentifier(selectedOption: Pick<ISelectedOption, 'id'>): number {
    return selectedOption.id;
  }

  compareSelectedOption(o1: Pick<ISelectedOption, 'id'> | null, o2: Pick<ISelectedOption, 'id'> | null): boolean {
    return o1 && o2 ? this.getSelectedOptionIdentifier(o1) === this.getSelectedOptionIdentifier(o2) : o1 === o2;
  }

  addSelectedOptionToCollectionIfMissing<Type extends Pick<ISelectedOption, 'id'>>(
    selectedOptionCollection: Type[],
    ...selectedOptionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const selectedOptions: Type[] = selectedOptionsToCheck.filter(isPresent);
    if (selectedOptions.length > 0) {
      const selectedOptionCollectionIdentifiers = selectedOptionCollection.map(
        selectedOptionItem => this.getSelectedOptionIdentifier(selectedOptionItem)!
      );
      const selectedOptionsToAdd = selectedOptions.filter(selectedOptionItem => {
        const selectedOptionIdentifier = this.getSelectedOptionIdentifier(selectedOptionItem);
        if (selectedOptionCollectionIdentifiers.includes(selectedOptionIdentifier)) {
          return false;
        }
        selectedOptionCollectionIdentifiers.push(selectedOptionIdentifier);
        return true;
      });
      return [...selectedOptionsToAdd, ...selectedOptionCollection];
    }
    return selectedOptionCollection;
  }

  protected convertDateFromClient<T extends ISelectedOption | NewSelectedOption | PartialUpdateSelectedOption>(
    selectedOption: T
  ): RestOf<T> {
    return {
      ...selectedOption,
      date: selectedOption.date?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restSelectedOption: RestSelectedOption): ISelectedOption {
    return {
      ...restSelectedOption,
      date: restSelectedOption.date ? dayjs(restSelectedOption.date) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSelectedOption>): HttpResponse<ISelectedOption> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSelectedOption[]>): HttpResponse<ISelectedOption[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
