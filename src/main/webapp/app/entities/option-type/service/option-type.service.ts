import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOptionType, NewOptionType } from '../option-type.model';

export type PartialUpdateOptionType = Partial<IOptionType> & Pick<IOptionType, 'id'>;

export type EntityResponseType = HttpResponse<IOptionType>;
export type EntityArrayResponseType = HttpResponse<IOptionType[]>;

@Injectable({ providedIn: 'root' })
export class OptionTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/option-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(optionType: NewOptionType): Observable<EntityResponseType> {
    return this.http.post<IOptionType>(this.resourceUrl, optionType, { observe: 'response' });
  }

  update(optionType: IOptionType): Observable<EntityResponseType> {
    return this.http.put<IOptionType>(`${this.resourceUrl}/${this.getOptionTypeIdentifier(optionType)}`, optionType, {
      observe: 'response',
    });
  }

  partialUpdate(optionType: PartialUpdateOptionType): Observable<EntityResponseType> {
    return this.http.patch<IOptionType>(`${this.resourceUrl}/${this.getOptionTypeIdentifier(optionType)}`, optionType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOptionType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOptionType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOptionTypeIdentifier(optionType: Pick<IOptionType, 'id'>): number {
    return optionType.id;
  }

  compareOptionType(o1: Pick<IOptionType, 'id'> | null, o2: Pick<IOptionType, 'id'> | null): boolean {
    return o1 && o2 ? this.getOptionTypeIdentifier(o1) === this.getOptionTypeIdentifier(o2) : o1 === o2;
  }

  addOptionTypeToCollectionIfMissing<Type extends Pick<IOptionType, 'id'>>(
    optionTypeCollection: Type[],
    ...optionTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const optionTypes: Type[] = optionTypesToCheck.filter(isPresent);
    if (optionTypes.length > 0) {
      const optionTypeCollectionIdentifiers = optionTypeCollection.map(optionTypeItem => this.getOptionTypeIdentifier(optionTypeItem)!);
      const optionTypesToAdd = optionTypes.filter(optionTypeItem => {
        const optionTypeIdentifier = this.getOptionTypeIdentifier(optionTypeItem);
        if (optionTypeCollectionIdentifiers.includes(optionTypeIdentifier)) {
          return false;
        }
        optionTypeCollectionIdentifiers.push(optionTypeIdentifier);
        return true;
      });
      return [...optionTypesToAdd, ...optionTypeCollection];
    }
    return optionTypeCollection;
  }
}
