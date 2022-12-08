import { IBooks } from 'app/entities/books/books.model';

export interface IBooksAttributes {
  id: number;
  code?: string | null;
  description?: string | null;
  isActive?: boolean | null;
  books?: Pick<IBooks, 'id'>[] | null;
}

export type NewBooksAttributes = Omit<IBooksAttributes, 'id'> & { id: null };
