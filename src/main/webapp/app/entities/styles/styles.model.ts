import { IOptions } from 'app/entities/options/options.model';

export interface IStyles {
  id: number;
  code?: string | null;
  description?: string | null;
  imgURL?: string | null;
  isActive?: boolean | null;
  options?: Pick<IOptions, 'id'>[] | null;
}

export type NewStyles = Omit<IStyles, 'id'> & { id: null };
