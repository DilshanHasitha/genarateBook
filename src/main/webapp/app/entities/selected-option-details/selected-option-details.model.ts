import { ISelectedOption } from 'app/entities/selected-option/selected-option.model';

export interface ISelectedOptionDetails {
  id: number;
  code?: string | null;
  name?: string | null;
  selectedValue?: string | null;
  isActive?: boolean | null;
  selectedOptions?: Pick<ISelectedOption, 'id'>[] | null;
}

export type NewSelectedOptionDetails = Omit<ISelectedOptionDetails, 'id'> & { id: null };
