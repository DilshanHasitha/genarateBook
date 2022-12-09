import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISelections, NewSelections } from '../selections.model';

export type PartialUpdateSelections = Partial<ISelections> & Pick<ISelections, 'id'>;

export type EntityResponseType = HttpResponse<ISelections>;
export type EntityArrayResponseType = HttpResponse<ISelections[]>;

@Injectable({ providedIn: 'root' })
export class SelectionsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/selections');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(selections: NewSelections): Observable<EntityResponseType> {
    return this.http.post<ISelections>(this.resourceUrl, selections, { observe: 'response' });
  }

  update(selections: ISelections): Observable<EntityResponseType> {
    return this.http.put<ISelections>(`${this.resourceUrl}/${this.getSelectionsIdentifier(selections)}`, selections, {
      observe: 'response',
    });
  }

  partialUpdate(selections: PartialUpdateSelections): Observable<EntityResponseType> {
    return this.http.patch<ISelections>(`${this.resourceUrl}/${this.getSelectionsIdentifier(selections)}`, selections, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISelections>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISelections[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSelectionsIdentifier(selections: Pick<ISelections, 'id'>): number {
    return selections.id;
  }

  compareSelections(o1: Pick<ISelections, 'id'> | null, o2: Pick<ISelections, 'id'> | null): boolean {
    return o1 && o2 ? this.getSelectionsIdentifier(o1) === this.getSelectionsIdentifier(o2) : o1 === o2;
  }

  addSelectionsToCollectionIfMissing<Type extends Pick<ISelections, 'id'>>(
    selectionsCollection: Type[],
    ...selectionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const selections: Type[] = selectionsToCheck.filter(isPresent);
    if (selections.length > 0) {
      const selectionsCollectionIdentifiers = selectionsCollection.map(selectionsItem => this.getSelectionsIdentifier(selectionsItem)!);
      const selectionsToAdd = selections.filter(selectionsItem => {
        const selectionsIdentifier = this.getSelectionsIdentifier(selectionsItem);
        if (selectionsCollectionIdentifiers.includes(selectionsIdentifier)) {
          return false;
        }
        selectionsCollectionIdentifiers.push(selectionsIdentifier);
        return true;
      });
      return [...selectionsToAdd, ...selectionsCollection];
    }
    return selectionsCollection;
  }
}
