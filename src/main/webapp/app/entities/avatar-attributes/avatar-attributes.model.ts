import { IAvatarCharactor } from 'app/entities/avatar-charactor/avatar-charactor.model';
import { IOptions } from 'app/entities/options/options.model';
import { IBooks } from 'app/entities/books/books.model';

export interface IAvatarAttributes {
  id: number;
  code?: string | null;
  description?: string | null;
  isActive?: boolean | null;
  avatarCharactors?: Pick<IAvatarCharactor, 'id'>[] | null;
  options?: Pick<IOptions, 'id'>[] | null;
  books?: Pick<IBooks, 'id'>[] | null;
}

export type NewAvatarAttributes = Omit<IAvatarAttributes, 'id'> & { id: null };
