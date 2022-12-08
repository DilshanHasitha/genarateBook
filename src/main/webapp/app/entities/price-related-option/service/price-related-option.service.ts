import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPriceRelatedOption, NewPriceRelatedOption } from '../price-related-option.model';

export type PartialUpdatePriceRelatedOption = Partial<IPriceRelatedOption> & Pick<IPriceRelatedOption, 'id'>;

export type EntityResponseType = HttpResponse<IPriceRelatedOption>;
export type EntityArrayResponseType = HttpResponse<IPriceRelatedOption[]>;

@Injectable({ providedIn: 'root' })
export class PriceRelatedOptionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/price-related-options');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(priceRelatedOption: NewPriceRelatedOption): Observable<EntityResponseType> {
    return this.http.post<IPriceRelatedOption>(this.resourceUrl, priceRelatedOption, { observe: 'response' });
  }

  update(priceRelatedOption: IPriceRelatedOption): Observable<EntityResponseType> {
    return this.http.put<IPriceRelatedOption>(
      `${this.resourceUrl}/${this.getPriceRelatedOptionIdentifier(priceRelatedOption)}`,
      priceRelatedOption,
      { observe: 'response' }
    );
  }

  partialUpdate(priceRelatedOption: PartialUpdatePriceRelatedOption): Observable<EntityResponseType> {
    return this.http.patch<IPriceRelatedOption>(
      `${this.resourceUrl}/${this.getPriceRelatedOptionIdentifier(priceRelatedOption)}`,
      priceRelatedOption,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPriceRelatedOption>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPriceRelatedOption[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPriceRelatedOptionIdentifier(priceRelatedOption: Pick<IPriceRelatedOption, 'id'>): number {
    return priceRelatedOption.id;
  }

  comparePriceRelatedOption(o1: Pick<IPriceRelatedOption, 'id'> | null, o2: Pick<IPriceRelatedOption, 'id'> | null): boolean {
    return o1 && o2 ? this.getPriceRelatedOptionIdentifier(o1) === this.getPriceRelatedOptionIdentifier(o2) : o1 === o2;
  }

  addPriceRelatedOptionToCollectionIfMissing<Type extends Pick<IPriceRelatedOption, 'id'>>(
    priceRelatedOptionCollection: Type[],
    ...priceRelatedOptionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const priceRelatedOptions: Type[] = priceRelatedOptionsToCheck.filter(isPresent);
    if (priceRelatedOptions.length > 0) {
      const priceRelatedOptionCollectionIdentifiers = priceRelatedOptionCollection.map(
        priceRelatedOptionItem => this.getPriceRelatedOptionIdentifier(priceRelatedOptionItem)!
      );
      const priceRelatedOptionsToAdd = priceRelatedOptions.filter(priceRelatedOptionItem => {
        const priceRelatedOptionIdentifier = this.getPriceRelatedOptionIdentifier(priceRelatedOptionItem);
        if (priceRelatedOptionCollectionIdentifiers.includes(priceRelatedOptionIdentifier)) {
          return false;
        }
        priceRelatedOptionCollectionIdentifiers.push(priceRelatedOptionIdentifier);
        return true;
      });
      return [...priceRelatedOptionsToAdd, ...priceRelatedOptionCollection];
    }
    return priceRelatedOptionCollection;
  }
}
