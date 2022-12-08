import dayjs from 'dayjs/esm';

import { ISelectedOption, NewSelectedOption } from './selected-option.model';

export const sampleWithRequiredData: ISelectedOption = {
  id: 23567,
};

export const sampleWithPartialData: ISelectedOption = {
  id: 62366,
  date: dayjs('2022-12-04'),
};

export const sampleWithFullData: ISelectedOption = {
  id: 31169,
  code: 'compress Incredible',
  date: dayjs('2022-12-05'),
};

export const sampleWithNewData: NewSelectedOption = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
