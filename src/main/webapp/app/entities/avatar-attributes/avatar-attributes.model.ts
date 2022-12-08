import { IAvatarCharactor } from 'app/entities/avatar-charactor/avatar-charactor.model';
import { IBooks } from 'app/entities/books/books.model';
import { IStyles } from 'app/entities/styles/styles.model';

export interface IAvatarAttributes {
  id: number;
  code?: string | null;
  description?: string | null;
  isActive?: boolean | null;
  avatarCharactors?: Pick<IAvatarCharactor, 'id'>[] | null;
  books?: Pick<IBooks, 'id'>[] | null;
  styles?: Pick<IStyles, 'id'>[] | null;
}

export type NewAvatarAttributes = Omit<IAvatarAttributes, 'id'> & { id: null };
