import dayjs from 'dayjs/esm';
import { IBooks } from 'app/entities/books/books.model';
import { ICustomer } from 'app/entities/customer/customer.model';
import { ISelectedOptionDetails } from 'app/entities/selected-option-details/selected-option-details.model';

export interface ISelectedOption {
  id: number;
  code?: string | null;
  date?: dayjs.Dayjs | null;
  books?: Pick<IBooks, 'id'> | null;
  customer?: Pick<ICustomer, 'id'> | null;
  selectedOptionDetails?: Pick<ISelectedOptionDetails, 'id'>[] | null;
}

export type NewSelectedOption = Omit<ISelectedOption, 'id'> & { id: null };
