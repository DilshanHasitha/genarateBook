import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPriceRelatedOptionDetails, NewPriceRelatedOptionDetails } from '../price-related-option-details.model';

export type PartialUpdatePriceRelatedOptionDetails = Partial<IPriceRelatedOptionDetails> & Pick<IPriceRelatedOptionDetails, 'id'>;

export type EntityResponseType = HttpResponse<IPriceRelatedOptionDetails>;
export type EntityArrayResponseType = HttpResponse<IPriceRelatedOptionDetails[]>;

@Injectable({ providedIn: 'root' })
export class PriceRelatedOptionDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/price-related-option-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(priceRelatedOptionDetails: NewPriceRelatedOptionDetails): Observable<EntityResponseType> {
    return this.http.post<IPriceRelatedOptionDetails>(this.resourceUrl, priceRelatedOptionDetails, { observe: 'response' });
  }

  update(priceRelatedOptionDetails: IPriceRelatedOptionDetails): Observable<EntityResponseType> {
    return this.http.put<IPriceRelatedOptionDetails>(
      `${this.resourceUrl}/${this.getPriceRelatedOptionDetailsIdentifier(priceRelatedOptionDetails)}`,
      priceRelatedOptionDetails,
      { observe: 'response' }
    );
  }

  partialUpdate(priceRelatedOptionDetails: PartialUpdatePriceRelatedOptionDetails): Observable<EntityResponseType> {
    return this.http.patch<IPriceRelatedOptionDetails>(
      `${this.resourceUrl}/${this.getPriceRelatedOptionDetailsIdentifier(priceRelatedOptionDetails)}`,
      priceRelatedOptionDetails,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPriceRelatedOptionDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPriceRelatedOptionDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPriceRelatedOptionDetailsIdentifier(priceRelatedOptionDetails: Pick<IPriceRelatedOptionDetails, 'id'>): number {
    return priceRelatedOptionDetails.id;
  }

  comparePriceRelatedOptionDetails(
    o1: Pick<IPriceRelatedOptionDetails, 'id'> | null,
    o2: Pick<IPriceRelatedOptionDetails, 'id'> | null
  ): boolean {
    return o1 && o2 ? this.getPriceRelatedOptionDetailsIdentifier(o1) === this.getPriceRelatedOptionDetailsIdentifier(o2) : o1 === o2;
  }

  addPriceRelatedOptionDetailsToCollectionIfMissing<Type extends Pick<IPriceRelatedOptionDetails, 'id'>>(
    priceRelatedOptionDetailsCollection: Type[],
    ...priceRelatedOptionDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const priceRelatedOptionDetails: Type[] = priceRelatedOptionDetailsToCheck.filter(isPresent);
    if (priceRelatedOptionDetails.length > 0) {
      const priceRelatedOptionDetailsCollectionIdentifiers = priceRelatedOptionDetailsCollection.map(
        priceRelatedOptionDetailsItem => this.getPriceRelatedOptionDetailsIdentifier(priceRelatedOptionDetailsItem)!
      );
      const priceRelatedOptionDetailsToAdd = priceRelatedOptionDetails.filter(priceRelatedOptionDetailsItem => {
        const priceRelatedOptionDetailsIdentifier = this.getPriceRelatedOptionDetailsIdentifier(priceRelatedOptionDetailsItem);
        if (priceRelatedOptionDetailsCollectionIdentifiers.includes(priceRelatedOptionDetailsIdentifier)) {
          return false;
        }
        priceRelatedOptionDetailsCollectionIdentifiers.push(priceRelatedOptionDetailsIdentifier);
        return true;
      });
      return [...priceRelatedOptionDetailsToAdd, ...priceRelatedOptionDetailsCollection];
    }
    return priceRelatedOptionDetailsCollection;
  }
}
