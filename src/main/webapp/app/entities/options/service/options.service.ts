import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOptions, NewOptions } from '../options.model';

export type PartialUpdateOptions = Partial<IOptions> & Pick<IOptions, 'id'>;

export type EntityResponseType = HttpResponse<IOptions>;
export type EntityArrayResponseType = HttpResponse<IOptions[]>;

@Injectable({ providedIn: 'root' })
export class OptionsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/options');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(options: NewOptions): Observable<EntityResponseType> {
    return this.http.post<IOptions>(this.resourceUrl, options, { observe: 'response' });
  }

  update(options: IOptions): Observable<EntityResponseType> {
    return this.http.put<IOptions>(`${this.resourceUrl}/${this.getOptionsIdentifier(options)}`, options, { observe: 'response' });
  }

  partialUpdate(options: PartialUpdateOptions): Observable<EntityResponseType> {
    return this.http.patch<IOptions>(`${this.resourceUrl}/${this.getOptionsIdentifier(options)}`, options, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOptions>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOptions[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOptionsIdentifier(options: Pick<IOptions, 'id'>): number {
    return options.id;
  }

  compareOptions(o1: Pick<IOptions, 'id'> | null, o2: Pick<IOptions, 'id'> | null): boolean {
    return o1 && o2 ? this.getOptionsIdentifier(o1) === this.getOptionsIdentifier(o2) : o1 === o2;
  }

  addOptionsToCollectionIfMissing<Type extends Pick<IOptions, 'id'>>(
    optionsCollection: Type[],
    ...optionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const options: Type[] = optionsToCheck.filter(isPresent);
    if (options.length > 0) {
      const optionsCollectionIdentifiers = optionsCollection.map(optionsItem => this.getOptionsIdentifier(optionsItem)!);
      const optionsToAdd = options.filter(optionsItem => {
        const optionsIdentifier = this.getOptionsIdentifier(optionsItem);
        if (optionsCollectionIdentifiers.includes(optionsIdentifier)) {
          return false;
        }
        optionsCollectionIdentifiers.push(optionsIdentifier);
        return true;
      });
      return [...optionsToAdd, ...optionsCollection];
    }
    return optionsCollection;
  }
}
