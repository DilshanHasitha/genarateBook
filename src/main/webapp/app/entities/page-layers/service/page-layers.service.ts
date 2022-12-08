import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPageLayers, NewPageLayers } from '../page-layers.model';

export type PartialUpdatePageLayers = Partial<IPageLayers> & Pick<IPageLayers, 'id'>;

export type EntityResponseType = HttpResponse<IPageLayers>;
export type EntityArrayResponseType = HttpResponse<IPageLayers[]>;

@Injectable({ providedIn: 'root' })
export class PageLayersService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/page-layers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pageLayers: NewPageLayers): Observable<EntityResponseType> {
    return this.http.post<IPageLayers>(this.resourceUrl, pageLayers, { observe: 'response' });
  }

  update(pageLayers: IPageLayers): Observable<EntityResponseType> {
    return this.http.put<IPageLayers>(`${this.resourceUrl}/${this.getPageLayersIdentifier(pageLayers)}`, pageLayers, {
      observe: 'response',
    });
  }

  partialUpdate(pageLayers: PartialUpdatePageLayers): Observable<EntityResponseType> {
    return this.http.patch<IPageLayers>(`${this.resourceUrl}/${this.getPageLayersIdentifier(pageLayers)}`, pageLayers, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPageLayers>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPageLayers[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPageLayersIdentifier(pageLayers: Pick<IPageLayers, 'id'>): number {
    return pageLayers.id;
  }

  comparePageLayers(o1: Pick<IPageLayers, 'id'> | null, o2: Pick<IPageLayers, 'id'> | null): boolean {
    return o1 && o2 ? this.getPageLayersIdentifier(o1) === this.getPageLayersIdentifier(o2) : o1 === o2;
  }

  addPageLayersToCollectionIfMissing<Type extends Pick<IPageLayers, 'id'>>(
    pageLayersCollection: Type[],
    ...pageLayersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pageLayers: Type[] = pageLayersToCheck.filter(isPresent);
    if (pageLayers.length > 0) {
      const pageLayersCollectionIdentifiers = pageLayersCollection.map(pageLayersItem => this.getPageLayersIdentifier(pageLayersItem)!);
      const pageLayersToAdd = pageLayers.filter(pageLayersItem => {
        const pageLayersIdentifier = this.getPageLayersIdentifier(pageLayersItem);
        if (pageLayersCollectionIdentifiers.includes(pageLayersIdentifier)) {
          return false;
        }
        pageLayersCollectionIdentifiers.push(pageLayersIdentifier);
        return true;
      });
      return [...pageLayersToAdd, ...pageLayersCollection];
    }
    return pageLayersCollection;
  }
}
