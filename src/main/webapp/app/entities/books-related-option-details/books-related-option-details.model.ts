import { IBooksRelatedOption } from 'app/entities/books-related-option/books-related-option.model';

export interface IBooksRelatedOptionDetails {
  id: number;
  code?: string | null;
  description?: string | null;
  isActive?: boolean | null;
  booksRelatedOptions?: Pick<IBooksRelatedOption, 'id'>[] | null;
}

export type NewBooksRelatedOptionDetails = Omit<IBooksRelatedOptionDetails, 'id'> & { id: null };
