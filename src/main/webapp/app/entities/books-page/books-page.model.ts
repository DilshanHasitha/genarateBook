import { IPageLayers } from 'app/entities/page-layers/page-layers.model';
import { IBooks } from 'app/entities/books/books.model';

export interface IBooksPage {
  id: number;
  num?: number | null;
  isActive?: boolean | null;
  pageDetails?: Pick<IPageLayers, 'id'>[] | null;
  books?: Pick<IBooks, 'id'>[] | null;
}

export type NewBooksPage = Omit<IBooksPage, 'id'> & { id: null };
