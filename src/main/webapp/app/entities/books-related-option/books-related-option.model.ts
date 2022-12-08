import { IBooksRelatedOptionDetails } from 'app/entities/books-related-option-details/books-related-option-details.model';
import { IBooks } from 'app/entities/books/books.model';

export interface IBooksRelatedOption {
  id: number;
  code?: string | null;
  name?: string | null;
  isActive?: boolean | null;
  booksRelatedOptionDetails?: Pick<IBooksRelatedOptionDetails, 'id'>[] | null;
  books?: Pick<IBooks, 'id'>[] | null;
}

export type NewBooksRelatedOption = Omit<IBooksRelatedOption, 'id'> & { id: null };
