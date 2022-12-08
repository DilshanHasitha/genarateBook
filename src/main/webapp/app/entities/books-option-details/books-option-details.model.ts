import { IBooks } from 'app/entities/books/books.model';

export interface IBooksOptionDetails {
  id: number;
  avatarAttributes?: string | null;
  avatarCharactor?: string | null;
  style?: string | null;
  option?: string | null;
  isActive?: boolean | null;
  books?: Pick<IBooks, 'id'> | null;
}

export type NewBooksOptionDetails = Omit<IBooksOptionDetails, 'id'> & { id: null };
