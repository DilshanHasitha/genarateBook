import { IPageLayersDetails } from 'app/entities/page-layers-details/page-layers-details.model';
import { IBooksPage } from 'app/entities/books-page/books-page.model';

export interface IPageLayers {
  id: number;
  layerNo?: number | null;
  isActive?: boolean | null;
  pageElementDetails?: Pick<IPageLayersDetails, 'id'>[] | null;
  booksPages?: Pick<IBooksPage, 'id'>[] | null;
}

export type NewPageLayers = Omit<IPageLayers, 'id'> & { id: null };
