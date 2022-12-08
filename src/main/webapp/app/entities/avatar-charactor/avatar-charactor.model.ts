import { IAvatarAttributes } from 'app/entities/avatar-attributes/avatar-attributes.model';

export interface IAvatarCharactor {
  id: number;
  code?: string | null;
  description?: string | null;
  isActive?: boolean | null;
  avatarAttributes?: Pick<IAvatarAttributes, 'id'>[] | null;
}

export type NewAvatarCharactor = Omit<IAvatarCharactor, 'id'> & { id: null };
