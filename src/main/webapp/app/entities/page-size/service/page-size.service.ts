import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPageSize, NewPageSize } from '../page-size.model';

export type PartialUpdatePageSize = Partial<IPageSize> & Pick<IPageSize, 'id'>;

export type EntityResponseType = HttpResponse<IPageSize>;
export type EntityArrayResponseType = HttpResponse<IPageSize[]>;

@Injectable({ providedIn: 'root' })
export class PageSizeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/page-sizes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pageSize: NewPageSize): Observable<EntityResponseType> {
    return this.http.post<IPageSize>(this.resourceUrl, pageSize, { observe: 'response' });
  }

  update(pageSize: IPageSize): Observable<EntityResponseType> {
    return this.http.put<IPageSize>(`${this.resourceUrl}/${this.getPageSizeIdentifier(pageSize)}`, pageSize, { observe: 'response' });
  }

  partialUpdate(pageSize: PartialUpdatePageSize): Observable<EntityResponseType> {
    return this.http.patch<IPageSize>(`${this.resourceUrl}/${this.getPageSizeIdentifier(pageSize)}`, pageSize, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPageSize>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPageSize[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPageSizeIdentifier(pageSize: Pick<IPageSize, 'id'>): number {
    return pageSize.id;
  }

  comparePageSize(o1: Pick<IPageSize, 'id'> | null, o2: Pick<IPageSize, 'id'> | null): boolean {
    return o1 && o2 ? this.getPageSizeIdentifier(o1) === this.getPageSizeIdentifier(o2) : o1 === o2;
  }

  addPageSizeToCollectionIfMissing<Type extends Pick<IPageSize, 'id'>>(
    pageSizeCollection: Type[],
    ...pageSizesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pageSizes: Type[] = pageSizesToCheck.filter(isPresent);
    if (pageSizes.length > 0) {
      const pageSizeCollectionIdentifiers = pageSizeCollection.map(pageSizeItem => this.getPageSizeIdentifier(pageSizeItem)!);
      const pageSizesToAdd = pageSizes.filter(pageSizeItem => {
        const pageSizeIdentifier = this.getPageSizeIdentifier(pageSizeItem);
        if (pageSizeCollectionIdentifiers.includes(pageSizeIdentifier)) {
          return false;
        }
        pageSizeCollectionIdentifiers.push(pageSizeIdentifier);
        return true;
      });
      return [...pageSizesToAdd, ...pageSizeCollection];
    }
    return pageSizeCollection;
  }
}
