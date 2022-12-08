import { IBooks } from 'app/entities/books/books.model';

export interface IBooksVariables {
  id: number;
  code?: string | null;
  description?: string | null;
  isActive?: boolean | null;
  books?: Pick<IBooks, 'id'>[] | null;
}

export type NewBooksVariables = Omit<IBooksVariables, 'id'> & { id: null };
