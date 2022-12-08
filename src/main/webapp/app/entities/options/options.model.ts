import { IStyles } from 'app/entities/styles/styles.model';
import { IAvatarAttributes } from 'app/entities/avatar-attributes/avatar-attributes.model';

export interface IOptions {
  id: number;
  code?: string | null;
  description?: string | null;
  imgURL?: string | null;
  isActive?: boolean | null;
  styles?: Pick<IStyles, 'id'>[] | null;
  avatarAttributes?: Pick<IAvatarAttributes, 'id'>[] | null;
}

export type NewOptions = Omit<IOptions, 'id'> & { id: null };
