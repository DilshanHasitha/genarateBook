import { IAvatarCharactor } from 'app/entities/avatar-charactor/avatar-charactor.model';

export interface ICharacter {
  id: number;
  code?: string | null;
  description?: string | null;
  isActive?: boolean | null;
  avatarCharactors?: Pick<IAvatarCharactor, 'id'>[] | null;
}

export type NewCharacter = Omit<ICharacter, 'id'> & { id: null };
