import { IPageSize, NewPageSize } from './page-size.model';

export const sampleWithRequiredData: IPageSize = {
  id: 17770,
};

export const sampleWithPartialData: IPageSize = {
  id: 31717,
  height: 73685,
};

export const sampleWithFullData: IPageSize = {
  id: 39381,
  code: 'Coordinator',
  description: 'Georgia connect deploy',
  isActive: true,
  width: 8301,
  height: 41651,
};

export const sampleWithNewData: NewPageSize = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
