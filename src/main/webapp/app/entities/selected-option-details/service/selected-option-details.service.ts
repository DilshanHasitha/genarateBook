import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISelectedOptionDetails, NewSelectedOptionDetails } from '../selected-option-details.model';

export type PartialUpdateSelectedOptionDetails = Partial<ISelectedOptionDetails> & Pick<ISelectedOptionDetails, 'id'>;

export type EntityResponseType = HttpResponse<ISelectedOptionDetails>;
export type EntityArrayResponseType = HttpResponse<ISelectedOptionDetails[]>;

@Injectable({ providedIn: 'root' })
export class SelectedOptionDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/selected-option-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(selectedOptionDetails: NewSelectedOptionDetails): Observable<EntityResponseType> {
    return this.http.post<ISelectedOptionDetails>(this.resourceUrl, selectedOptionDetails, { observe: 'response' });
  }

  update(selectedOptionDetails: ISelectedOptionDetails): Observable<EntityResponseType> {
    return this.http.put<ISelectedOptionDetails>(
      `${this.resourceUrl}/${this.getSelectedOptionDetailsIdentifier(selectedOptionDetails)}`,
      selectedOptionDetails,
      { observe: 'response' }
    );
  }

  partialUpdate(selectedOptionDetails: PartialUpdateSelectedOptionDetails): Observable<EntityResponseType> {
    return this.http.patch<ISelectedOptionDetails>(
      `${this.resourceUrl}/${this.getSelectedOptionDetailsIdentifier(selectedOptionDetails)}`,
      selectedOptionDetails,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISelectedOptionDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISelectedOptionDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSelectedOptionDetailsIdentifier(selectedOptionDetails: Pick<ISelectedOptionDetails, 'id'>): number {
    return selectedOptionDetails.id;
  }

  compareSelectedOptionDetails(o1: Pick<ISelectedOptionDetails, 'id'> | null, o2: Pick<ISelectedOptionDetails, 'id'> | null): boolean {
    return o1 && o2 ? this.getSelectedOptionDetailsIdentifier(o1) === this.getSelectedOptionDetailsIdentifier(o2) : o1 === o2;
  }

  addSelectedOptionDetailsToCollectionIfMissing<Type extends Pick<ISelectedOptionDetails, 'id'>>(
    selectedOptionDetailsCollection: Type[],
    ...selectedOptionDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const selectedOptionDetails: Type[] = selectedOptionDetailsToCheck.filter(isPresent);
    if (selectedOptionDetails.length > 0) {
      const selectedOptionDetailsCollectionIdentifiers = selectedOptionDetailsCollection.map(
        selectedOptionDetailsItem => this.getSelectedOptionDetailsIdentifier(selectedOptionDetailsItem)!
      );
      const selectedOptionDetailsToAdd = selectedOptionDetails.filter(selectedOptionDetailsItem => {
        const selectedOptionDetailsIdentifier = this.getSelectedOptionDetailsIdentifier(selectedOptionDetailsItem);
        if (selectedOptionDetailsCollectionIdentifiers.includes(selectedOptionDetailsIdentifier)) {
          return false;
        }
        selectedOptionDetailsCollectionIdentifiers.push(selectedOptionDetailsIdentifier);
        return true;
      });
      return [...selectedOptionDetailsToAdd, ...selectedOptionDetailsCollection];
    }
    return selectedOptionDetailsCollection;
  }
}
